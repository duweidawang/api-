package com.duwss.apibackend.controller;


import com.duwss.apibackend.common.ErrorCode;
import com.duwss.apibackend.exception.BusinessException;
import com.duwss.apibackend.model.dto.interfaceinfo.InterfaceInvokeRequest;
import com.duwss.apibackend.model.entity.InterfaceInfo;

import com.duwss.apibackend.model.entity.model.enums.InterfaceInfoStatusEnum;
import com.duwss.apibackend.service.InterfaceInfoService;
import com.duwss.apibackend.service.UserService;
import com.duwss.duwssapisdk.client.DuwssClient;
import com.duwss.duwssapisdk.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/dapi")
public class DuwssApiController {

    @Resource
    DuwssClient duwssClient;

    @Resource
    UserService userService;

    @Resource
    InterfaceInfoService interfaceInfoService;



    @GetMapping("/name")
    public String getName(){
        String duwie = duwssClient.getUserName("duwie");
        return duwie;

    }


    @PostMapping("/name")
    public String userName(@RequestBody InterfaceInvokeRequest interfaceInvokeRequest, HttpServletRequest request){
        //校验
        if(interfaceInvokeRequest==null || interfaceInvokeRequest.getId()<0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        Long id = interfaceInvokeRequest.getId();
        String userName = interfaceInvokeRequest.getUserName();
        User requestParam = new User();
       requestParam.setUsername(interfaceInvokeRequest.getUserName());
       requestParam.setInterfaceId(interfaceInvokeRequest.getId());



        //判断接口是否存在
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(id);
        if(interfaceInfo ==null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 判断状态是否可用
        if(interfaceInfo.getStatus()== InterfaceInfoStatusEnum.OFFLINE.getValue()){
            throw  new BusinessException(ErrorCode.PARAMS_ERROR,"接口状态不可用");
        }

        com.duwss.apibackend.model.entity.User loginUser = userService.getLoginUser(request);
        String accessKey = loginUser.getAccessKey();
        String secretKey = loginUser.getSecretKey();


        DuwssClient duwssClient = new DuwssClient(accessKey,secretKey);

        String result = duwssClient.postUser(requestParam);

        return result;

    }



}
