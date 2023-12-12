package com.duwss.apigateway.feign;


import com.duwss.common.common.BaseResponse;
import com.duwss.common.model.entity.InterfaceInfo;
import com.duwss.common.model.entity.User;

import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(name = "duwssbackend",path = "api")
public interface UserInformationFeign {

    @GetMapping("/user/getinterfaceUser")
    public BaseResponse<com.duwss.common.model.entity.User> getUser(@RequestParam("accessKey")  String accessKey);
    @GetMapping("/InterfaceInfo/remoteinterfaceinfo")
    public BaseResponse remoteinterface(@RequestParam("path") String path, @RequestParam("method") String method);

    @GetMapping("/uinterface/remoteuserinterinfo")
    public BaseResponse remoteUserInterInfo(@RequestParam("userId") Long userId,@RequestParam("interfaceInfoId") Long interfaceInfoId);

    @GetMapping("/uinterface/invokecount")
    public BaseResponse remoteInvokeCount(@RequestParam("userId") Long userId,@RequestParam("interfaceInfoId") Long interfaceInfoId);



    }