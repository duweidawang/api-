package com.duwss.apibackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.duwss.apibackend.model.entity.InterfaceInfo;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 杜伟
* @description 针对表【interface_info(接口信息)】的数据库操作Mapper
* @createDate 2023-11-04 21:41:25
* @Entity com.duwss.personbackend.model.entity.InterfaceInfo
*/

@Mapper
public interface InterfaceInfoMapper extends BaseMapper<InterfaceInfo> {

}




