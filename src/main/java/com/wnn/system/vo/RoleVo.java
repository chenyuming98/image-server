package com.wnn.system.vo;

import com.wnn.system.domain.system.Permission;
import com.wnn.system.domain.system.Role;
import lombok.Data;

import java.util.List;

@Data
public class RoleVo extends Role {

    private List<Permission> permissions;
}
