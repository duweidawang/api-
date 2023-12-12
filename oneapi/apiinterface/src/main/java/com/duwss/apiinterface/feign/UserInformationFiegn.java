package com.duwss.apiinterface.feign;


import com.duwss.common.common.BaseResponse;
import com.duwss.common.model.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "duwssbackend",path = "api")
public interface UserInformationFiegn {

    @GetMapping("/user/getinterfaceUser")
    public BaseResponse<User> getUser(@RequestParam("accessKey") String accessKey);
}
