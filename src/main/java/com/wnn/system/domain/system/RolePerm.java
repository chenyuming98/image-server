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
public class RolePerm {
    @Id
    private String rolePermId;

    private String roleId;

    private String permId;
}