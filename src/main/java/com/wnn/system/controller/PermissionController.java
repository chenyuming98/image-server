package com.wnn.system.controller;

import com.wnn.system.common.annotation.SysLog;
import com.wnn.system.common.base.controller.BaseController;
import com.wnn.system.domain.system.Permission;
import com.wnn.system.vo.MenuTreeVo;
import com.wnn.system.domain.base.BaseResult;
import com.wnn.system.domain.base.ResultCode;
import com.wnn.system.service.PermissionService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 系统设置-权限管理模块
 */
@RestController
@RequestMapping("/system/permission")
@Api(description = "权限管理")
public class PermissionController extends BaseController {

    @Autowired
    private PermissionService permissionService;

    /**
     * 系统管理模块-菜单管理，左侧权限树，右侧编辑表框；左侧权限管理树包含信息 菜单+权限
     */
    @GetMapping("/menuTreeList")
    public BaseResult findMenuAndPermTreeList(){
        List<MenuTreeVo> menuTreeList = permissionService.findMenuAndPermTreeList(this.userObject);
        return new BaseResult(ResultCode.OK ,menuTreeList);
    }

    /**
     * 新增权限
     */
    @SysLog("新增权限")
    @PostMapping()
    public BaseResult addPermission(@RequestBody Permission permission){
        String permissionId  = permissionService.addPermission(permission);
        return new BaseResult(ResultCode.OK ,permissionId);
    }


    /**
     * 删除一个权限/菜单
     * @param id
     * @return
     */
    @SysLog("删除一个权限/菜单")
    @DeleteMapping("/{id}")
    public BaseResult deletePermission(@PathVariable(value = "id")String id){
        int i = permissionService.deletePermissionById(id);
        if (i>0){
            return new BaseResult(ResultCode.OK);
        }
        return  new BaseResult(ResultCode.FALSE);
    }


    /**
     * 批量删除权限菜单
     * @param ids
     * @return
     */
    @SysLog("批量删除权限菜单")
    @RequestMapping(value="/deletes",method = RequestMethod.POST)
    public BaseResult batchDeletePerm(String ids) {
        int i = permissionService.batchDeletePerm(ids);
        if (i==0){
            return  new BaseResult(ResultCode.FALSE);
        }
        return new BaseResult(ResultCode.OK);
    }

    /**
     * 更新一个权限
     */
    @SysLog("更新权限")
    @PutMapping("/{id}")
    public BaseResult updatePermission(@PathVariable(value = "id")String id,@RequestBody Permission permission){
        permission.setPermissionId(id);
        int i = permissionService.updatePermission(permission);
        if (i>0){
            return new BaseResult(ResultCode.OK);
        }
        return  new BaseResult(ResultCode.FALSE);
    }

}
