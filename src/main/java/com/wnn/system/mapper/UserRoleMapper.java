package com.wnn.system.mapper;
import com.wnn.system.common.base.dao.IBaseDao;
import com.wnn.system.domain.system.UserAndRole;
import com.wnn.system.domain.system.UserRoleExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserRoleMapper extends IBaseDao<UserAndRole> {
    long countByExample(UserRoleExample example);

    int deleteByExample(UserRoleExample example);

    List<UserAndRole> selectByExample(UserRoleExample example);

    int updateByExampleSelective(@Param("record") UserAndRole record, @Param("example") UserRoleExample example);

    int updateByExample(@Param("record") UserAndRole record, @Param("example") UserRoleExample example);
}