package com.wnn.system.domain.system;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import java.util.Date;

/**
* Created by Mybatis Generator on 2020/03/15
*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    @Id
    private String roleId;

    /**
     * 权限名称
     */
    private String roleName;


    /**
     * 权限编码
     */
    private String roleCode;

    /**
     * 描述
     */
    private String description;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}