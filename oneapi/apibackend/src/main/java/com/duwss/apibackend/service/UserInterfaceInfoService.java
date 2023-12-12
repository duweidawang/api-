package com.duwss.apibackend.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.duwss.apibackend.common.BaseResponse;
import com.duwss.apibackend.model.dto.userinterface.UInterAddRequest;
import com.duwss.apibackend.model.entity.UserInterfaceInfo;

/**
* @author 杜伟
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Service
* @createDate 2023-11-11 17:01:28
*/
public interface UserInterfaceInfoService extends IService<UserInterfaceInfo> {




     UserInterfaceInfo getInterfaceUser(String accessKey);
     void validPost(UserInterfaceInfo userInterfaceInfo, boolean ok);

    BaseResponse getUserInterfaceInfo(Long userId, Long interfaceInfoId);

    BaseResponse invokeCount(Long userId, Long interfaceInfoId);
}
