package com.duwss.apibackend.model.dto.interfaceinfo;


import com.duwss.duwssapisdk.entity.User;
import lombok.Data;

import java.io.Serializable;

@Data
public class InterfaceInvokeRequest implements Serializable {

    private Long id;

    private String userName;

}
