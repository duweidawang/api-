package com.duwss.apibackend.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.duwss.apibackend.model.entity.UserInterfaceInfo;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 杜伟
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Mapper
* @createDate 2023-11-11 17:01:28
*
*/
@Mapper
public interface UserInterfaceInfoMapper extends BaseMapper<UserInterfaceInfo> {

}




