package com.wnn.system.domain.system.vo;

import com.wnn.system.common.annotation.Dict;
import com.wnn.system.domain.system.Role;
import com.wnn.system.domain.system.User;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Data
@Getter
@Setter
public class UserVo extends User {

    /**
     * 性别
     */
    @Dict(dicDataSource = "sex",dicText = "")
    private String sex;
    private Set<Role> roles;
    private Set<String> roleIds;

}