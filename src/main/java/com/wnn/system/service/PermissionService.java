package com.wnn.system.service;

import com.wnn.system.domain.system.Role;
import com.wnn.system.domain.system.vo.UserVo;
import com.wnn.system.frame.advice.GlobalException;
import com.wnn.system.common.utils.IdWorker;
import com.wnn.system.domain.system.Permission;
import com.wnn.system.domain.system.PermissionExample;
import com.wnn.system.common.utils.MyStringUtil;
import com.wnn.system.mapper.RoleMapper;
import com.wnn.system.vo.MenuTreeVo;
import com.wnn.system.domain.base.ResultCode;
import com.wnn.system.mapper.PermissionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Transient;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 系统设置-权限管理模块
 */
@Service
public class PermissionService {

    @Autowired
    private PermissionMapper permissionMapper;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private RoleMapper roleMapper;

    /**
     * 系统管理模块，左侧权限树，右侧编辑表框；左侧权限管理树包含信息 菜单+权限
     */
    public List<MenuTreeVo>  findMenuAndPermTreeList(UserVo userVo) {
        String userId = userVo.getUserId();
        if (MyStringUtil.isNullOrEmpty(userId)){
            throw new GlobalException(ResultCode.PLEASE_LOGIN);
        }
        Set<Role> roleSet = roleMapper.selectRoleListByUserId(userId);
        //如果是管理员获取全部菜单权限
        boolean isAdmin = roleSet.stream()
                                .anyMatch(role -> "admin".equals(role.getRoleCode()));
        List< MenuTreeVo > permAll;
        if (isAdmin){
            permAll = permissionMapper.selectPermAll();
        }else {
            permAll = permissionMapper.selectPermTreeVoByUserId(userId);
        }
        // stream 流处理
        List<MenuTreeVo> list = permAll.stream()
                .filter(p -> "0".equals(p.getParentId()) || "".equals(p.getParentId()))
                .peek((menuTreeVo -> menuTreeVo.setChildren(this.getChild(menuTreeVo.getPermissionId(), permAll))))
                .sorted(Comparator.comparingInt(Permission::getSortNumber))
                .collect(Collectors.toList());
        return list;
    }

    /**
     * 递归获取某菜单下所有子节点
     */
    private List< MenuTreeVo > getChild(String parentPermId,List< MenuTreeVo > permAll) {
        return permAll.stream().filter(p -> parentPermId.equals(p.getParentId()))
                .peek((menuTreeVo -> menuTreeVo.setChildren(this.getChild(menuTreeVo.getPermissionId(), permAll))))
                .collect(Collectors.toList());
    }
    /**
     * 新增权限
     */
    @Transient
    public String  addPermission(Permission permission) {
        String title = permission.getTitle();
        if (title==null||"".equals(title)){
            throw new GlobalException(ResultCode.FALSE);
        }
        String newId = idWorker.nextId() ;
        if (permission.getType()==2){
            permission.setSpread(1);
            permission.setEnable(1);
        }
        permission.setPermissionId(newId);
        Date date = new Date();
        permission.setCreateTime(date);
        permissionMapper.insert(permission);
        return newId;
    }

    /**
     * 删除一个权限/菜单
     */
    @Transient
    public int deletePermissionById(String id) {
        Permission permission = permissionMapper.selectByPrimaryKey(id);
        if (permission==null){
            throw new GlobalException(ResultCode.NULL,"权限不存在");
        }
        return permissionMapper.deleteByPrimaryKey(id);
    }

    /**
     * 更新一个权限
     */
    @Transient
    public int updatePermission(Permission permissionView) {
        PermissionExample permissionExample = new PermissionExample();
        permissionExample.createCriteria().andPermissionIdEqualTo(permissionView.getPermissionId());
        if ( permissionView.getType() ==null ){
            throw new GlobalException(ResultCode.NULL,"权限类型不能为空");
        }
        if (permissionView.getSortNumber() ==null){
            permissionView.setSortNumber(100);
        }
        return permissionMapper.updateByExampleSelective(permissionView,permissionExample);
    }

    /**
     * 批量删除权限菜单
     */
    @Transient
    public int batchDeletePerm(String ids) {
        String idsKey = ids;
        System.out.println("1------"+idsKey);
        idsKey = idsKey.replace(",","','");
        System.out.println("2------"+idsKey);
        idsKey = "'" + idsKey + "'";
        System.out.println("3------"+idsKey);
        return  permissionMapper.deleteByIds(idsKey);
    }

}
