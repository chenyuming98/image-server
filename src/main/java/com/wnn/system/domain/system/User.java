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
public class User {
    @Id
    private String userId;

    /**
     * 登录用户名
     */
    private String username;

    /**
     * 密码 
     */
    private String userPassword;

    /**
     * 启用状态 (0正常，1冻结)
     */
    private Integer userStatus;

    /**
     * 员工ID
     */
    private String employeeId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}