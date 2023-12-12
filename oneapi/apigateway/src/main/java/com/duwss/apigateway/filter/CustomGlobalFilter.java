package com.duwss.apigateway.filter;


import cn.hutool.core.util.StrUtil;

import com.alibaba.fastjson2.JSON;

import com.duwss.apigateway.entity.UserInterfaceInfo;
import com.duwss.apigateway.feign.UserInformationFeign;
import com.duwss.common.common.BaseResponse;
import com.duwss.common.model.entity.InterfaceInfo;
import com.duwss.common.model.entity.User;


import com.duwss.duwssapisdk.utils.SignUtils;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;

import org.springframework.http.HttpHeaders;

import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Component
@Slf4j
public class CustomGlobalFilter implements GlobalFilter, Ordered {
    private static final String ADDRESS = "localhost:8123";

    @Autowired
    UserInformationFeign userInformationFeign;



    //设置白名单
    private static final List<String> WRITE_NAME = Arrays.asList("127.0.0.1");



    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 1请求日志

        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath().value();
        String hostString = request.getLocalAddress().getHostString();


        String method = request.getMethod().toString();
        log.info("请求方法" + method);
        log.info("请求路径" + path);
        log.info("请求参数" + request.getQueryParams());
        log.info("请求来源地址" + hostString);
        log.info("请求来源地址" + request.getRemoteAddress());

        //访问控制-黑白名单
        ServerHttpResponse response = exchange.getResponse();
        if (!WRITE_NAME.contains(hostString)) {
            response.setStatusCode(HttpStatus.FORBIDDEN);
            return response.setComplete();
        }
        //用户的鉴权    ak sk
        //先获取请求头中的内容
        HttpHeaders headers = request.getHeaders();
        String accessKey = headers.getFirst("accessKey");
        String nonce = headers.getFirst("randNum");
        String timestamp = headers.getFirst("timestamp");
        String sign = headers.getFirst("sign");
        String body = headers.getFirst("body");
        //判断是否有空
        boolean b = StrUtil.hasBlank(accessKey, timestamp, sign, body, nonce);
        if(b){
            response.setStatusCode(HttpStatus.FORBIDDEN);
            return response.setComplete();

        }
        //1.需要先拿ak去查询用户信息；
        BaseResponse<User> user = userInformationFeign.getUser(accessKey);
        User userData = user.getData();
        String secretKey = userData.getSecretKey();
        String sign1 = SignUtils.genSign(body, secretKey);

        //校验sign是否相等
        if(!sign1.equals(sign)){
            response.setStatusCode(HttpStatus.FORBIDDEN);
            return response.setComplete();
        }

        //校验时间，防重放 时间五分钟
        String now = String.valueOf(System.currentTimeMillis() / 1000);
        Long FIVE_MINUTES = 60*5L;
        if(Long.parseLong(now)-Long.parseLong(timestamp)>=FIVE_MINUTES){
            response.setStatusCode(HttpStatus.FORBIDDEN);
            return response.setComplete();
        }

        //首先得查询到这个接口的信息，在此处通过path和method去查询到接口信息
        InterfaceInfo interfaceInfo =null;
        try{
            BaseResponse remoteinterface = userInformationFeign.remoteinterface(ADDRESS+path, method);
            Object data1 = remoteinterface.getData();
            String string = JSON.toJSONString(data1);
            interfaceInfo =  JSON.parseObject(string,InterfaceInfo.class);

        }
        catch (Exception e){
            log.error("getInvokeUser error", e);
        }
        //检验是否分配给用户
        if(interfaceInfo!=null){
            Long interfaceId = interfaceInfo.getId();
            Long userId = userData.getId();
            BaseResponse baseResponse = userInformationFeign.remoteUserInterInfo(userId, interfaceId);
            Object data = baseResponse.getData();
            //判断接口是否分配给用户
            if(data==null){
                response.setStatusCode(HttpStatus.FORBIDDEN);
                return response.setComplete();
            }
            String string = JSON.toJSONString(data);

            UserInterfaceInfo userInterfaceInfo = JSON.parseObject(string, UserInterfaceInfo.class);
            //判断接口是否还有剩余调用次数
            if(userInterfaceInfo.getLeftNum()==0){
               response.setStatusCode(HttpStatus.FORBIDDEN);
               return response.setComplete();
            }
            //如果还有则放行进行调用，等成功以后进行接口调用次数减一
            //直接这样放行，是一个异步操作
        }


        return handleResponse(exchange,chain,userData.getId(),interfaceInfo.getId());
    }



    public Mono<Void> handleResponse(ServerWebExchange exchange, GatewayFilterChain chain,Long userId,Long interfaceInfoId){
        try {
            ServerHttpResponse originalResponse = exchange.getResponse();
            // 缓存数据的工厂
            DataBufferFactory bufferFactory = originalResponse.bufferFactory();
            // 拿到响应码
            HttpStatus statusCode = originalResponse.getStatusCode();
            if (statusCode == HttpStatus.OK) {
                // 装饰，增强能力
                ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
                    // 等调用完转发的接口后才会执行
                    @Override
                    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                        log.info("body instanceof Flux: {}", (body instanceof Flux));
                        if (body instanceof Flux) {
                            Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                            // 往返回值里写数据
                            // 拼接字符串
                            return super.writeWith(
                                    fluxBody.map(dataBuffer -> {
//                                        // 7. 调用成功，接口调用次数 + 1 invokeCount
                                        try{
                                            userInformationFeign.remoteInvokeCount(userId,interfaceInfoId);
                                        }catch (Exception e){
                                            throw new RuntimeException();
                                        }

                                        //如果有计费，该可以扣钱
                                        byte[] content = new byte[dataBuffer.readableByteCount()];
                                        dataBuffer.read(content);
                                        DataBufferUtils.release(dataBuffer);//释放掉内存
                                        // 构建日志
                                        StringBuilder sb2 = new StringBuilder(200);
                                        List<Object> rspArgs = new ArrayList<>();
                                        rspArgs.add(originalResponse.getStatusCode());
                                        String data = new String(content, StandardCharsets.UTF_8); //data
                                        sb2.append(data);
                                        // 打印日志
                                        log.info("响应结果：" + data);
                                        return bufferFactory.wrap(content);
                                    }));
                        } else {
                            // 8. 调用失败，返回一个规范的错误码
                            log.error("<--- {} 响应code异常", getStatusCode());
                        }
                        return super.writeWith(body);
                    }
                };
                // 设置 response 对象为装饰过的
                return chain.filter(exchange.mutate().response(decoratedResponse).build());
            }
            return chain.filter(exchange); // 降级处理返回数据
        } catch (Exception e) {
            log.error("网关处理响应异常" + e);
            return chain.filter(exchange);
        }

    }














    public Mono<Void> handleNoAuth(ServerHttpResponse response){
        response.setStatusCode(HttpStatus.FORBIDDEN);
        return response.setComplete();
    }


    @Override
    public int getOrder() {
        return -2;
    }
}
