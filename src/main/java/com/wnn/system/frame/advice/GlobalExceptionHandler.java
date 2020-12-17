package com.wnn.system.frame.advice;

import com.wnn.system.domain.base.BaseResult;
import com.wnn.system.domain.base.ResultCode;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * 全局异常捕捉
 */
@ControllerAdvice
public class GlobalExceptionHandler {


    /**
     * 全局自动捕获运行异常处理
     * @param e
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public BaseResult handleException(Exception e) {
        e.printStackTrace();
        System.out.println("全局异常捕获");
        if (e.getCause() instanceof GlobalException){
            GlobalException my = (GlobalException) e.getCause();
            ResultCode resultCode = my.getResultCode();
            return new BaseResult(resultCode.getCode(),my.getMsg());
        }
        if (e  instanceof IncorrectCredentialsException){
            return new BaseResult(400,"密码错误");
        }
        if (e  instanceof NullPointerException){
            return new BaseResult(400,"执行异常");
        }
        if (e  instanceof UnauthorizedException){
            return new BaseResult(500,"权限不足！");
        }
        return new BaseResult(500,"执行异常！");

    }


    /**
     * 抛出自定义异常处理
     * @param globalException
     */
    @ExceptionHandler(GlobalException.class)
    @ResponseBody
    public BaseResult handleMyException(GlobalException globalException) {
        globalException.printStackTrace();
        ResultCode resultCode = globalException.getResultCode();
        String msg = globalException.getMsg();
        System.out.println("自定义异常信息："+msg);
        return  new BaseResult(resultCode.getCode(),msg);
    }



}