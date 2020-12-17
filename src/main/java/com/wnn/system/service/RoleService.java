package com.wnn.system.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wnn.system.frame.advice.GlobalException;
import com.wnn.system.common.utils.IdWorker;
import com.wnn.system.domain.system.Permission;
import com.wnn.system.domain.system.Role;
import com.wnn.system.domain.system.RolePerm;
import com.wnn.system.domain.base.ResultCode;
import com.wnn.system.mapper.RoleMapper;
import com.wnn.system.vo.RoleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Transient;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * 系统设置-角色管理模块
 */
@Service
public class RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private IdWorker idWorker;

    /**
     *  新增角色,同时可以分配权限
     */
    @Transient
    public int addRole(RoleVo roleVo) {
        if (StringUtils.isEmpty(roleVo.getRoleName())){
            throw  new GlobalException(ResultCode.NULL);
        }
        String roleId = idWorker.nextId() ;
        roleVo.setRoleId(roleId);
        roleVo.setCreateTime(new Date());
        int result = roleMapper.insert( roleVo);
        this.assignRolePerm(roleId,roleVo.getPermissions());
        return result;
    }

    /**
     *  角色列表查询
     */
    public PageInfo< RoleVo > findRoleList(Integer page, Integer limit) {
        PageHelper.startPage(page,limit);
        List<RoleVo> roles = roleMapper.selectRoleAll();
        List<RoleVo> roleList = roleMapper.findRoleListByRoleIds(roles);
        return new PageInfo<>(roleList);
    }

    /**
     * 根据Id删除角色
     */
    @Transient
    public int deleteRoleById(String id) {
        return roleMapper.deleteByPrimaryKey(id);
    }

    /**
     *  保存角色,同时分配权限
     */
    @Transient
    public int saveRole(String id, RoleVo role) {
        role.setRoleId(id);
        role.setCreateTime(null);
        role.setUpdateTime(new Date());
        int result = roleMapper.updateByPrimaryKeySelective(role);
        this.assignRolePerm(id,role.getPermissions());
        return result;

    }


    /**
     * 分配角色拥有的权限
     */
    @Transient
    private void assignRolePerm(String roleId,List<Permission> permissionList) {
        //存储前端更改的ids列表
        ArrayList< String >  ids = new ArrayList<>();
        //存储数据库role_perm已有的permId
        ArrayList<String> dataIds = new ArrayList<>();
        //根据角色ID查找出角色拥有的中间表信息 role_perm
        List<RolePerm> roleAndPermDataList = roleMapper.findRoleAndPermList(roleId, null);
        //把前端传递的Permission列表转成 id存入 ids列表中做对比准备
        for (Permission p:permissionList
             ) {
            ids.add(p.getPermissionId());
        }
        for (RolePerm p:roleAndPermDataList
        ) {
            dataIds.add(p.getPermId());
        }
        //创建对比的新增列表和删除列表
        ArrayList< String > deleteIds = new ArrayList<>();
        ArrayList<  String > insertIds = new ArrayList<>();
        // 对比获得删除ids
        for (String id: dataIds  ) {
            // contains 包含方法 如果 id在列表ids中包含返回true
            if (!ids.contains(id)){
                deleteIds.add(id);
            }
        }
        //对比获取增加ids
        for (String id: ids  ) {
            if (!dataIds.contains(id)){
                insertIds.add(id);
            }
        }
//      批量增加
        List<Map> mapInsertList = new ArrayList<>();
        for (String id: insertIds
             ) {
            Map<String,String> map = new HashMap<>(16);
            map.put("id",idWorker.nextId() );
            map.put("permId",id);
            mapInsertList.add(map);
        }
        if (mapInsertList.size()>0){
             roleMapper.batchInsertRoleAndPerm(roleId,mapInsertList);
        }
        if (deleteIds.size()>0){
             roleMapper.batchDeleteRoleAndPerm(roleId,deleteIds);
        }
    }

    /**
     * 批量删除角色
     * @param ids
     * @return
     */
    @Transient
    public int batchDeleteRoleByIds(String ids) {
        return  roleMapper.deleteByIds(ids);
    }

    /**
     *  查询所有角色
     */
    public List<Role> findRoleAll() {
        return roleMapper.selectAll();
    }
}
