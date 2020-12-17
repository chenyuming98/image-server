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
public class Permission {

    @Id
    private String permissionId;

    private String title;

    /**
     * 1菜单，0权限
     */
    private Integer type;

    private String parentId;

    private Integer sortNumber;

    private String icon;

    private String hrefMethod;

    private String href="";

    /**
     * 菜单是否展开（1展开，0关闭）
     */
    private Integer spread;

    private String code;

    /**
     * 是否可见(1可见0不可见)
     */
    private Integer enable;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

}