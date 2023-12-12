package com.duwss.apibackend.model.dto.userinterface;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.duwss.apibackend.common.PageRequest;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class UInterQueryRequset extends PageRequest implements Serializable {

    /**
     * 调用用户 id
     */
    private Long userId;

    /**
     * 接口 id
     */
    private Long interfaceInfoId;


    /**
     * 0-正常，1-禁用
     */
    private Integer status;




    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}
