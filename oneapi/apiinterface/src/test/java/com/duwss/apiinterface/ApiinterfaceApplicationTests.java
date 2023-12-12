package com.duwss.apiinterface;

import com.alibaba.nacos.shaded.org.checkerframework.checker.units.qual.A;
import com.duwss.apiinterface.feign.UserInformationFiegn;
import com.duwss.common.common.BaseResponse;
import com.duwss.common.model.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ApiinterfaceApplicationTests {
    @Autowired
    UserInformationFiegn userInformationFiegn;

    @Test
    void contextLoads() {
        BaseResponse<User> abd = userInformationFiegn.getUser("abd");
        System.out.println(abd);
    }

}
