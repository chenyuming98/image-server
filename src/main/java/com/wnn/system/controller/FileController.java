package com.wnn.system.controller;

import com.github.pagehelper.PageInfo;
import com.wnn.system.common.annotation.SysLog;
import com.wnn.system.common.utils.UtilConstants;
import com.wnn.system.common.utils.ZipUtil;
import com.wnn.system.domain.base.BaseResult;
import com.wnn.system.domain.base.ResultCode;
import com.wnn.system.domain.image.Forecast;
import com.wnn.system.domain.image.Image;
import com.wnn.system.domain.image.Svm;
import com.wnn.system.frame.advice.GlobalException;
import com.wnn.system.service.FileService;
import com.wnn.system.vo.FileVo;
import com.wnn.system.vo.SvmVo;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;


/**
 * 文件管理模块
 */
@RestController
@RequestMapping("/image/")
@Api(description = "文件管理")
public class FileController {

    @Autowired
    private FileService fileService;


    /**
     * 获取文件目录
     */
    @SysLog("获取文件目录")
    @GetMapping("/file")
    public BaseResult findFileList(){
        List<FileVo> list = fileService.findFileList();
        return new BaseResult(ResultCode.OK ,list);
    }

    /**
     * 根据分拣目录获取图片信息
     */
    @SysLog("根据分拣目录获取图片信息")
    @GetMapping("/imageFile")
    public BaseResult findImageList(String filePath,
                               @RequestParam(defaultValue = "1")Integer page,
                              @RequestParam(defaultValue = "10")Integer size){
        PageInfo<Image> pageInfo = fileService.findList(filePath,page, size);
        return new BaseResult(ResultCode.OK,pageInfo);
    }


    /**
     * 上传文件压缩包
     */
    @SysLog("上传文件压缩包")
    @PostMapping("/uploadZip")
    @ResponseBody
    public BaseResult uploadZip(@RequestParam("file") MultipartFile multipartFile,String savePath){
        if(multipartFile == null){
            return  BaseResult.error();
        }
        File fileObject = null;
        try {
            String originalFilename = multipartFile.getOriginalFilename().split("\\.")[0];
            String path = UtilConstants.ProjectPath.PATH + savePath + "\\" +originalFilename;
            fileObject = File.createTempFile("zipTemp", null);
            multipartFile.transferTo(fileObject);
            ZipUtil zipUtil = new ZipUtil();
            zipUtil.zipUncompress(fileObject,path);
        } catch (Exception e) {
            e.printStackTrace();
            throw new GlobalException(ResultCode.FALSE,"文件错误");
        }
        return  BaseResult.ok();
    }


    /**
    * 批量删除图片
    */
    @SysLog("批量删除图片")
    @RequestMapping(value="/deletes",method = RequestMethod.POST)
    public BaseResult batchDelete(@RequestBody List<String> url) {
        int i = fileService.batchDelete(url);
        if (i==0){
            return BaseResult.failure();
        }
        return BaseResult.ok();
    }


    /**
     * SVM模型训练
     */
    @SysLog("SVM模型训练")
    @PostMapping("/trainSvm")
    public BaseResult trainSvm(@RequestBody SvmVo svmVo){
        fileService.trainSvm(svmVo);
        return new BaseResult(ResultCode.OK );
    }

    /**
     * 获取SVM文件列表
     */
    @SysLog("获取SVM文件列表")
    @GetMapping("/svmList")
    public BaseResult svmList(){
        List<Svm> list = fileService.svmList();
        return new BaseResult(ResultCode.OK ,list);
    }

    /**
     * 训练器信息-获取SVM文件列表管理
     */
    @SysLog("训练器信息-获取SVM文件列表管理")
    @GetMapping("/svmInfoList")
    public BaseResult svmInfoList( @RequestParam(defaultValue = "1")Integer page,
                                   @RequestParam(defaultValue = "10")Integer size){
        PageInfo<Svm> pageInfo =  fileService.svmInfoList(page,size);
        return new BaseResult(ResultCode.OK ,pageInfo);
    }


    /**
     * 批量删除SVM文件
     */
    @SysLog("批量删除SVM文件")
    @RequestMapping(value="/deletesSvmList",method = RequestMethod.POST)
    public BaseResult deletesSvmList(String ids) {
        int i = fileService.deletesSvmList(ids);
        if (i==0){
            return BaseResult.failure();
        }
        return BaseResult.ok();
    }

    /**
     * 测试集测试、训练结果
     */
    @SysLog("测试集测试、训练结果")
    @PostMapping("/svmTest")
    public BaseResult svmTest(@RequestBody Map<String,Object> submitInfo){
        System.out.println(submitInfo);
        fileService.svmTest(submitInfo);
        return new BaseResult(ResultCode.OK );
    }


    /**
     * 测试集测试、训练结果
     */
    @SysLog("测试集测试、训练结果")
    @GetMapping("/probabilityList")
    public BaseResult probabilityList(
                                      @RequestParam(defaultValue = "1")Integer page,
                                      @RequestParam(defaultValue = "10")Integer size) {
        PageInfo<Forecast> pageInfo = fileService.probabilityList(page, size);
        return new BaseResult(ResultCode.OK, pageInfo);

    }

    /**
     * 批量删除预测信息
     */
    @SysLog("批量删除预测信息")
    @RequestMapping(value="/deletesProbability",method = RequestMethod.POST)
    public BaseResult batchDeleteProbability(String ids) {
        int i = fileService.batchDeleteProbability(ids);
        if (i==0){
            return BaseResult.failure();
        }
        return BaseResult.ok();
    }


    /**
     * 上传一张、多张图片分类检测
     */
    @SysLog("图片分类检测")
    @PostMapping("/uploadImages")
    @ResponseBody
    public BaseResult uploadImages(@RequestParam("file") MultipartFile[] multipartFiles,String svmId){
        if(multipartFiles == null || multipartFiles.length==0){
            return new BaseResult(ResultCode.FALSE, "图片为空");
        }
        fileService.uploadImages(multipartFiles, svmId);
        return  BaseResult.ok();
    }
}
