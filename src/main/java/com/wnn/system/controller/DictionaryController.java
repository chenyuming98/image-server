package com.wnn.system.controller;

import com.github.pagehelper.PageInfo;
import com.wnn.system.common.annotation.SysLog;
import com.wnn.system.domain.system.Dictionary;
import com.wnn.system.domain.base.BaseResult;
import com.wnn.system.domain.base.ResultCode;
import com.wnn.system.service.DictionaryService;
import com.wnn.system.vo.DictionaryVo;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统设置-字典管理模块
 */
@RestController
@RequestMapping("/system/dictionary")
@Api(description = "字典管理")
public class DictionaryController {

    @Autowired
    private DictionaryService dictionaryService;

    /**
     *  字典列表查询
     */
    @GetMapping
    public BaseResult findDictionaryList(@RequestParam(defaultValue = "1")Integer page,
                                   @RequestParam(defaultValue = "10")Integer size){
        PageInfo<DictionaryVo> pageInfo = dictionaryService.findDictionaryList(page, size);
        return new BaseResult(ResultCode.OK,pageInfo);
    }

    /**
     *  字典父级列表查询,用于添加、编辑的下拉框
     */
    @GetMapping("/allParent")
    public BaseResult findDictionaryAllParentList( ){
        List<Dictionary> dictionaryList= dictionaryService.findDictionaryAllParentList();
        return new BaseResult(ResultCode.OK,dictionaryList);
    }

    /**
     *  根据字典code获取字典列表
     */
    @GetMapping("/findCodeList")
    public BaseResult findCodeList(@RequestParam String code){
        List<Dictionary> dictionaryList= dictionaryService.findCodeList(code);
        return new BaseResult(ResultCode.OK,dictionaryList);
    }

    /**
     *  新增字典
     */
    @SysLog("新增字典")
    @PostMapping()
    public BaseResult addDictionary(@RequestBody Dictionary  dictionary ){
        dictionaryService.addDictionary(dictionary );
        return BaseResult.ok();
    }

    /**
     *  保存字典,同时可以分配权限
     */
    @SysLog("保存字典")
    @RequestMapping(value = "/{id}",method = RequestMethod.PUT)
    public BaseResult saveDictionary(@PathVariable(value = "id")String id,@RequestBody Dictionary  dictionary ){
        dictionaryService.saveDictionary(id,dictionary );
        return BaseResult.ok();
    }

    /**
     * 批量删除字典
     */
    @SysLog("批量删除字典")
    @RequestMapping(value="/deletes",method = RequestMethod.POST)
    public BaseResult deletes(String ids) {
        int i = dictionaryService.batchDeleteDictionaryByIds(ids);
        return BaseResult.ok();
    }


}
