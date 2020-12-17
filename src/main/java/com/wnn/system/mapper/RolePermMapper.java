package com.wnn.system.mapper;

import com.wnn.system.common.base.dao.IBaseDao;
import com.wnn.system.domain.system.RolePerm;
import com.wnn.system.domain.system.RolePermExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* Created by Mybatis Generator on 2020/03/15
*/
public interface RolePermMapper extends IBaseDao<RolePerm>{
    long countByExample(RolePermExample example);

    int deleteByExample(RolePermExample example);

    List<RolePerm> selectByExample(RolePermExample example);

    int updateByExampleSelective(@Param("record") RolePerm record, @Param("example") RolePermExample example);

    int updateByExample(@Param("record") RolePerm record, @Param("example") RolePermExample example);
}