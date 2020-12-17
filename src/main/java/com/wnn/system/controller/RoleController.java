package com.wnn.system.controller;

import com.github.pagehelper.PageInfo;
import com.wnn.system.common.annotation.SysLog;
import com.wnn.system.domain.system.Role;
import com.wnn.system.domain.base.BaseResult;
import com.wnn.system.domain.base.ResultCode;
import com.wnn.system.service.RoleService;
import com.wnn.system.vo.RoleVo;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统设置-角色管理模块
 */
@RestController
@RequestMapping("/system/role")
@Api(description = "角色管理")
public class RoleController {

    @Autowired
    private RoleService roleService;

    /**
     *  角色列表查询
     */
    @GetMapping
    public BaseResult findRoleList(@RequestParam(defaultValue = "1")Integer page,
                              @RequestParam(defaultValue = "10")Integer size){
        PageInfo<RoleVo> pageInfo = roleService.findRoleList(page, size);
        return new BaseResult(ResultCode.OK,pageInfo);
    }


    /**
     *  查询所有角色
     */
    @GetMapping("findRoleAll")
    public BaseResult findRoleAll(){
        List<Role> roleAll= roleService.findRoleAll( );
        return new BaseResult(ResultCode.OK, roleAll);
    }

    /**
     *  新增角色,同时可以分配权限
     */
    @SysLog("新增角色")
    @PostMapping()
    public BaseResult addRole(@RequestBody RoleVo roleVo){
        int i = roleService.addRole(roleVo);
        if (i>0){
            return BaseResult.ok();
        }
        return BaseResult.failure();
    }

    /**
     *  保存角色,同时可以分配权限
     */
    @SysLog("保存角色")
    @RequestMapping(value = "/{id}",method = RequestMethod.PUT)
    public BaseResult saveRole(@PathVariable(value = "id")String id,@RequestBody RoleVo roleVo){
        int i = roleService.saveRole(id,roleVo);
        if (i>0){
            return BaseResult.ok();
        }
        return BaseResult.failure();
    }

    /**
     * 批量删除角色
     */
    @SysLog("批量删除角色")
    @RequestMapping(value="/deletes",method = RequestMethod.POST)
    public BaseResult batchDeleteUserByIds(String ids) {
        int i = roleService.batchDeleteRoleByIds(ids);
        if (i==0){
            return BaseResult.failure();
        }
        return BaseResult.ok();
    }


    /**
     * 根据Id删除角色
     */
    @SysLog("删除角色")
    @RequestMapping(value = "/{id}",method = RequestMethod.DELETE)
    public BaseResult deleteRoleById(@PathVariable(value = "id")String id){
        int i = roleService.deleteRoleById(id);
        if (i>0){
            return BaseResult.ok();
        }
        return BaseResult.failure();
    }


}
