package com.wnn.system.vo;

import com.wnn.system.domain.system.Permission;
import lombok.Data;

import java.util.List;

@Data
public class MenuTreeVo extends Permission {

    private String roleName;
    private List<MenuTreeVo> children;
}
