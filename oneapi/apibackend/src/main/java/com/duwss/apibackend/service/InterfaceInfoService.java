package com.duwss.apibackend.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.duwss.apibackend.common.BaseResponse;
import com.duwss.apibackend.model.entity.InterfaceInfo;

/**
* @author 杜伟
* @description 针对表【interface_info(接口信息)】的数据库操作Service
* @createDate 2023-11-04 21:41:25
*/
public interface InterfaceInfoService extends IService<InterfaceInfo> {

    void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add);

    BaseResponse getInfoByPath(String path, String method);
}
