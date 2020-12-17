package com.wnn.system.controller;

import com.github.pagehelper.PageInfo;
import com.wnn.system.common.annotation.SysLog;
import com.wnn.system.common.base.controller.BaseController;
import com.wnn.system.domain.base.BaseResult;
import com.wnn.system.domain.base.ResultCode;
import com.wnn.system.domain.system.Syslog;
import com.wnn.system.service.SysLogService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;



/**
 * 系统设置-日志管理模块
 */
@RestController
@RequestMapping("/system/sysLog")
@Api(description = "日志管理")
public class SysLogController extends BaseController {

    @Autowired
    private SysLogService sysLogService;

    /**
     *  日志列表查询
     */
    @GetMapping
    public BaseResult findSysLogList(@RequestParam(defaultValue = "1")Integer page, @RequestParam(defaultValue = "10")Integer size
                                     ,@RequestParam(required = false)   String username,@RequestParam(required = false)String result
                                         ,@RequestParam(required = false)String ipAddr  ,@RequestParam(required = false)String createTime
                                            ,@RequestParam(required = false)String createTimeEnd){
        PageInfo<Syslog> sysLogList = sysLogService.findSysLogList( username, result, ipAddr
                                                    , createTime, createTimeEnd,page, size);
        return new BaseResult(ResultCode.OK,sysLogList);
    }


    /**
     * 批量删除日志
     */
    @SysLog("批量删除日志")
    @RequestMapping(value="/deletes",method = RequestMethod.POST)
    public BaseResult batchDeleteUserByIds(String ids) {
        int i = sysLogService.batchDeleteSysLogByIds(ids);
        return BaseResult.ok();
    }


}
