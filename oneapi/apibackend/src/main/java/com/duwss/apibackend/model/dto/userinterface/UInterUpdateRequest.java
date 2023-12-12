package com.duwss.apibackend.model.dto.userinterface;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.util.Date;

@Data
public class UInterUpdateRequest {

    /**
     * 主键
     */

    private Long id;

    /**
     * 总调用次数
     */
    private Integer totalNum;

    /**
     * 0-正常，1-禁用
     */
    private Integer status;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}
