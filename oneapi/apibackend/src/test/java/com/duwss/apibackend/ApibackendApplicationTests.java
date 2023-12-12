package com.duwss.apibackend;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.alibaba.nacos.shaded.org.checkerframework.checker.units.qual.A;
import com.duwss.apibackend.common.BaseResponse;
import com.duwss.apibackend.model.entity.InterfaceInfo;
import com.duwss.apibackend.service.InterfaceInfoService;
import com.duwss.apibackend.service.UserInterfaceInfoService;
import com.duwss.apibackend.service.UserService;
import com.duwss.duwssapisdk.entity.User;
import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.lang.reflect.Constructor;

@SpringBootTest
class ApibackendApplicationTests {
    @Autowired
    InterfaceInfoService interfaceInfoService;
    @Autowired
    UserInterfaceInfoService userInterfaceInfoService;

    @Test
    void test(){
        BaseResponse get = interfaceInfoService.getInfoByPath("localhost:8080/api/user", "GET");
        Object data = get.getData();
        InterfaceInfo i = (InterfaceInfo) data;
        System.out.println(i);
    }

    @Test
    void testincoke(){
        BaseResponse baseResponse = userInterfaceInfoService.invokeCount(1L, 61L);


    }

    @Test
    void contextLoads() throws ClassNotFoundException {


    }

    @Resource
    UserService userService;

    @Test
    public void selectByAc(){
        User user = new User();
        user.setUsername("suewe");
        String json =  JSONUtil.toJsonStr(user);//http://localhost:8999/api/"http://localhost:8999"+"/api/user/name"
        HttpResponse result2 = HttpRequest.post("http://localhost:8999/api/user/name")

                .body(json)
                .execute();
        System.out.println(result2.getStatus());

        System.out.println(result2.body());

    }
    @Test
    public void select(){
        com.duwss.apibackend.model.entity.User abd = userService.getInterByac("abd");
        System.out.println(abd);


    }





}
