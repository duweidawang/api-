package com.duwss.apibackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.duwss.apibackend.common.BaseResponse;
import com.duwss.apibackend.common.ErrorCode;
import com.duwss.apibackend.common.ResultUtils;
import com.duwss.apibackend.exception.BusinessException;
import com.duwss.apibackend.mapper.InterfaceInfoMapper;
import com.duwss.apibackend.model.entity.InterfaceInfo;
import com.duwss.apibackend.service.InterfaceInfoService;
import org.springframework.stereotype.Service;

import javax.management.Query;

/**
* @author 杜伟
* @description 针对表【interface_info(接口信息)】的数据库操作Service实现
* @createDate 2023-11-04 21:41:25
*/
@Service
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo>
    implements InterfaceInfoService {

    @Override
    public void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add) {

        if (interfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

//        // 创建时，所有参数必须非空
//        if (add) {
//            if (StringUtils.isAnyBlank(content, job, place, education, loveExp) || ObjectUtils.anyNull(age, gender)) {
//                throw new BusinessException(ErrorCode.PARAMS_ERROR);
//            }
//        }
//        if (StringUtils.isNotBlank(content) && content.length() > 8192) {
//            throw new BusinessException(ErrorCode.PARAMS_ERROR, "内容过长");
//        }
//        if (reviewStatus != null && !interfaceInfoReviewStatusEnum.getValues().contains(reviewStatus)) {
//            throw new BusinessException(ErrorCode.PARAMS_ERROR);
//        }
//        if (age != null && (age < 18 || age > 100)) {
//            throw new BusinessException(ErrorCode.PARAMS_ERROR, "年龄不符合要求");
//        }
//        if (gender != null && !interfaceInfoGenderEnum.getValues().contains(gender)) {
//            throw new BusinessException(ErrorCode.PARAMS_ERROR, "性别不符合要求");
//        }
    }

    /**
     * 根据path，和method远程返回接口信息
     * @param path
     * @param method
     * @return
     */
    @Override
    public BaseResponse getInfoByPath(String path, String method) {
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("url",path);
        queryWrapper.eq("method",method);
        InterfaceInfo one = this.getOne(queryWrapper);
        return ResultUtils.success(one);
    }

}





