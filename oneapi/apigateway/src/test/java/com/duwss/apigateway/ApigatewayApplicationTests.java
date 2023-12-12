package com.duwss.apigateway;


import cn.hutool.json.JSONUtil;

import com.alibaba.fastjson2.JSON;
import com.alibaba.nacos.shaded.org.checkerframework.checker.units.qual.A;
import com.duwss.apigateway.feign.UserInformationFeign;
import com.duwss.common.common.BaseResponse;
import com.duwss.common.model.entity.InterfaceInfo;
import com.duwss.common.model.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;

@SpringBootTest
class ApigatewayApplicationTests {

    @Autowired
    UserInformationFeign userInformationFeign;


    @Test
    void contextLoads() {
        BaseResponse<User> abd = userInformationFeign.getUser("abd");
        System.out.println(abd.getData());


    }
}
