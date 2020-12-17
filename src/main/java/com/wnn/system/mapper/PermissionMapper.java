package com.wnn.system.mapper;

import com.wnn.system.common.base.dao.IBaseDao;
import com.wnn.system.domain.system.Permission;
import com.wnn.system.domain.system.PermissionExample;
import com.wnn.system.vo.MenuTreeVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* Created by Mybatis Generator on 2020/03/15
*/
public interface PermissionMapper extends IBaseDao<Permission>{
    long countByExample(PermissionExample example);

    int deleteByExample(PermissionExample example);

    List<Permission> selectByExample(PermissionExample example);

    int updateByExampleSelective(@Param("record") Permission record, @Param("example") PermissionExample example);

    int updateByExample(@Param("record") Permission record, @Param("example") PermissionExample example);

    /**
     * 得到的菜单权限
     * @return  MenuTreeVo 树结构的包装类
     */
    List< MenuTreeVo > selectAllPermTreeVo();

    /**
     * 获取所有权限
     * @return
     */
    List<MenuTreeVo> selectPermAll();

    /**
     * 得到的菜单权限
     * @return  MenuTreeVo 树结构的包装类
     */
    List< MenuTreeVo > selectPermTreeVoByUserId(@Param("userId") String userId);
}