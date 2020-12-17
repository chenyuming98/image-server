package com.wnn.system.mapper;

import com.wnn.system.common.base.dao.IBaseDao;
import com.wnn.system.domain.system.User;
import com.wnn.system.domain.system.UserAndRole;
import com.wnn.system.domain.system.UserExample;
import com.wnn.system.domain.system.vo.UserVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
* Created by Mybatis Generator on 2020/03/15
*/
public interface UserMapper extends IBaseDao<User> {
    long countByExample(UserExample example);

    int deleteByExample(UserExample example);

    List<User> selectByExample(UserExample example);

    int updateByExampleSelective(@Param("record") User record, @Param("example") UserExample example);

    int updateByExample(@Param("record") User record, @Param("example") UserExample example);

    List<Map<String, String>> selectUserHaveRoles();

    /**
     * 查询用户列表包含角色信息
     */
    List<UserVo> findUserList();

    /**
     * 根据用户id找到中间表user_role包含用户id的所有信息
     */
    List<UserAndRole> selectUserAndRoleListByUserId(@Param("userId") String userId);

    /**
     * 批量新增user_role中间表
     */
    int batchInsertUserAndRoleList(@Param("userAndRoleList") List< UserAndRole > userAndRoleList);

    /**
     * 批量删除user_role中间表数据 根据用户id和角色Id
     */
    int batchDeleteUserAndRoleListByUserIdAndRoleIds(@Param("userId") String userId, @Param("ids") List<String>  ids);

    User selectUserByUsername(@Param("username") String username);

}