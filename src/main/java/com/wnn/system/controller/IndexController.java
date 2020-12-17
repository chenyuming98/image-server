package com.wnn.system.controller;

import com.wnn.system.domain.base.BaseResult;
import com.wnn.system.domain.base.ResultCode;
import com.wnn.system.service.IndexService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * 主页信息
 */
@RestController
@RequestMapping("/indexInfo")
@Api(description = "主页信息")
public class IndexController {

    @Autowired
    private IndexService indexService;

    /**
     * 获取标签信息
     */
    @GetMapping("/label")
    public BaseResult getLabelInfo(){
        HashMap<String,Object> resultMap = indexService.getLabelInfo();
        return new BaseResult(ResultCode.OK,resultMap);
    }

    /**
     * 获取统计
     */
    @GetMapping("/getStaticInfo")
    public BaseResult getStaticInfo(){
        HashMap<String,Object> resultMap = indexService.getStaticInfo();
        return new BaseResult(ResultCode.OK,resultMap);
    }
}
