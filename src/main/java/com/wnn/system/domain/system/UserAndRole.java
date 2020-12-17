package com.wnn.system.domain.system;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;

/**
* Created by Mybatis Generator on 2020/03/15
*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAndRole {
    @Id
    private String userRoleId;

    private String userId;

    private String roleId;


}