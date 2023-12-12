package com.duwss.apibackend.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.duwss.apibackend.common.BaseResponse;
import com.duwss.apibackend.common.ErrorCode;
import com.duwss.apibackend.common.ResultUtils;
import com.duwss.apibackend.exception.BusinessException;
import com.duwss.apibackend.mapper.UserInterfaceInfoMapper;
import com.duwss.apibackend.model.entity.User;
import com.duwss.apibackend.model.entity.UserInterfaceInfo;
import com.duwss.apibackend.service.UserInterfaceInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
* @author 杜伟
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Service实现
* @createDate 2023-11-11 17:01:28
*/
@Service
public class UserInterfaceInfoServiceImpl extends ServiceImpl<UserInterfaceInfoMapper, UserInterfaceInfo> implements UserInterfaceInfoService {


    @Override
    public UserInterfaceInfo getInterfaceUser(String accessKey) {
        if(!StringUtils.isNotEmpty(accessKey)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<UserInterfaceInfo> userInterfaceInfoQueryWrapper = new QueryWrapper<>();
        userInterfaceInfoQueryWrapper.eq("accessKey",accessKey);
        UserInterfaceInfo userInterfaceInfo = this.getOne(userInterfaceInfoQueryWrapper);
        return userInterfaceInfo;

    }

    @Override
    public void validPost(UserInterfaceInfo userInterfaceInfo, boolean ok) {

    }

    /**
     * 运存返回接口用户信息
     * @param userId
     * @param interfaceInfoId
     * @return
     */
    @Override
    public BaseResponse getUserInterfaceInfo(Long userId, Long interfaceInfoId) {
        QueryWrapper<UserInterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId",userId).eq("interfaceInfoId",interfaceInfoId);
        UserInterfaceInfo one = this.getOne(queryWrapper);
        return ResultUtils.success(one);
    }



    //调用接口后，调用剩余接口减一
    @Override
    public BaseResponse invokeCount(Long userId, Long interfaceInfoId) {

        QueryWrapper<UserInterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId",userId).eq("interfaceInfoId",interfaceInfoId);
        UserInterfaceInfo one = this.getOne(queryWrapper);
        UserInterfaceInfo updateOne = new UserInterfaceInfo();

        updateOne.setLeftNum(one.getLeftNum()-1);

        UpdateWrapper<UserInterfaceInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("userId",userId).eq("interfaceInfoId",interfaceInfoId);

        boolean update = this.update(updateOne, updateWrapper);

        return ResultUtils.success("ok");
    }




}





