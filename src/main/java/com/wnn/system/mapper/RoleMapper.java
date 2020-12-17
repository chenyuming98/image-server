package com.wnn.system.mapper;

import com.wnn.system.common.base.dao.IBaseDao;
import com.wnn.system.domain.system.Role;
import com.wnn.system.domain.system.RolePerm;
import com.wnn.system.vo.RoleVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
* Created by Mybatis Generator on 2020/03/15
*/
public interface RoleMapper  extends IBaseDao<Role>{


    /**
     *  查询角色列表，包含权限
     */
    List<RoleVo> findRoleListByRoleIds(@Param("roles") List<RoleVo> roles);

    /**
     *   根据角色ID查找角色拥有的权限中间表
     */
    List< RolePerm > findRoleAndPermList(@Param("roleId") String roleId, String parentMenuId);

    /**
     * 批量分配 角色权限表
     */
    int batchInsertRoleAndPerm(@Param("roleId") String roleId, @Param("mapList") List<Map>  mapList);

    /**
     * 批量删除 角色权限表
     */
    int batchDeleteRoleAndPerm(@Param("roleId") String roleId, @Param("ids") List<String>  ids);

    /**
     * 返回roleVo包装类
     */
    List<RoleVo> selectRoleAll();

    /**
     *  根据用户ID查找角色Set列表
     */
    Set<Role> selectRoleListByUserId(@Param("userId") String userId);
}

