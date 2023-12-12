package com.duwss.apibackend.controller;




import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.duwss.apibackend.common.BaseResponse;

import com.duwss.apibackend.common.ErrorCode;
import com.duwss.apibackend.common.ResultUtils;
import com.duwss.apibackend.constant.CommonConstant;
import com.duwss.apibackend.exception.BusinessException;


import com.duwss.apibackend.model.dto.userinterface.UInterAddRequest;
import com.duwss.apibackend.model.dto.userinterface.UInterDeleteRequest;
import com.duwss.apibackend.model.dto.userinterface.UInterQueryRequset;
import com.duwss.apibackend.model.dto.userinterface.UInterUpdateRequest;

import com.duwss.apibackend.model.entity.UserInterfaceInfo;
import com.duwss.apibackend.service.UserInterfaceInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


@Slf4j
@RestController
@RequestMapping("/uinterface")
public class UserInterfaceController {
    @Resource
    UserInterfaceInfoService userInterfaceInfoService;


    /**
     * 创建
     *
     * @param uInterAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addPost(@RequestBody UInterAddRequest uInterAddRequest, HttpServletRequest request) {
        if (uInterAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserInterfaceInfo userInterfaceInfo =  new UserInterfaceInfo();
        BeanUtils.copyProperties(uInterAddRequest, userInterfaceInfo);
        // 校验
//        userInterfaceInfoService.validPost(userInterfaceInfo, true);
        
        boolean result = userInterfaceInfoService.save(userInterfaceInfo);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        long newPostId = userInterfaceInfo.getId();
        return ResultUtils.success(newPostId);
    }

    /**
     * 删除
     *
     * @param uInterDeleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deletePost(@RequestBody UInterDeleteRequest uInterDeleteRequest, HttpServletRequest request) {
        if (uInterDeleteRequest == null || uInterDeleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        
        // 判断是否存在
        UserInterfaceInfo byId = userInterfaceInfoService.getById(uInterDeleteRequest.getId());
        if (byId == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
       
        boolean b = userInterfaceInfoService.removeById(uInterDeleteRequest.getId());
        return ResultUtils.success(b);
    }

    /**
     * 更新
     *
     * @param uInterUpdateRequest
     * @param request
     * @return
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> updatePost(@RequestBody UInterUpdateRequest uInterUpdateRequest,
                                            HttpServletRequest request) {
        if (uInterUpdateRequest == null || uInterUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
      UserInterfaceInfo userInterfaceInfo = new UserInterfaceInfo();
        BeanUtils.copyProperties(uInterUpdateRequest, userInterfaceInfo);
        // 参数校验
//        userInterfaceInfoService.validPost(userInterfaceInfo, false);
      
        long id = uInterUpdateRequest.getId();
        // 判断是否存在
        UserInterfaceInfo byId = userInterfaceInfoService.getById(id);
        if (byId == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
      
        boolean result = userInterfaceInfoService.updateById(userInterfaceInfo);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    public BaseResponse<UserInterfaceInfo> getPostById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserInterfaceInfo byId = userInterfaceInfoService.getById(id);
        return ResultUtils.success(byId);
    }


    /**
     * 分页获取列表
     *
     * @param uInterQueryRequest
     * @param request
     * @return
     */
    @GetMapping("/list/page")
    public BaseResponse<Page<UserInterfaceInfo>> listPostByPage(UInterQueryRequset uInterQueryRequest, HttpServletRequest request) {
        if (uInterQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserInterfaceInfo userInterfaceInfo = new UserInterfaceInfo();
        BeanUtils.copyProperties(uInterQueryRequest, userInterfaceInfo);
        long current = uInterQueryRequest.getCurrent();
        long size = uInterQueryRequest.getPageSize();
        //排序字段
        String sortField = uInterQueryRequest.getSortField();
        //升序还是降序
        String sortOrder = uInterQueryRequest.getSortOrder();

        // 限制爬虫
        if (size > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<UserInterfaceInfo> queryWrapper = new QueryWrapper<>(userInterfaceInfo);
        if(userInterfaceInfo.getStatus()!=null){
            queryWrapper.ge("status",userInterfaceInfo.getStatus());
        }
        if(userInterfaceInfo.getUserId()!=null){
            queryWrapper.ge("userId",userInterfaceInfo.getUserId());
        }


        queryWrapper.orderBy(StringUtils.isNotBlank(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        Page<UserInterfaceInfo> postPage = userInterfaceInfoService.page(new Page<>(current, size), queryWrapper);
        return ResultUtils.success(postPage);
    }


    @GetMapping("/remoteuserinterinfo")
    public BaseResponse remoteUserInterInfo(@RequestParam("userId") Long userId,@RequestParam("interfaceInfoId") Long interfaceInfoId){
        return  userInterfaceInfoService.getUserInterfaceInfo(userId,interfaceInfoId);
    }

    @GetMapping("/invokecount")
    public BaseResponse remoteInvokeCount(@RequestParam("userId") Long userId,@RequestParam("interfaceInfoId") Long interfaceInfoId){

        return  userInterfaceInfoService.invokeCount(userId,interfaceInfoId);
    }





}
