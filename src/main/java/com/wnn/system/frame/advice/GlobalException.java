package com.wnn.system.frame.advice;
import com.wnn.system.domain.base.ResultCode;
import lombok.Getter;
import lombok.NoArgsConstructor;


/**
 * 抛出自定义异常
 * 形式1、 throw  new GlobalException(ResultCode.error);
 * 形式2、 throw GlobalException.error();   抛出静态方法
 */
@Getter
@NoArgsConstructor
public class GlobalException extends RuntimeException{

    private ResultCode resultCode;
    private String  msg;

    public GlobalException(ResultCode resultCode){
        this.resultCode = resultCode;
        this.msg = resultCode.getMsg();
    }

    public GlobalException(ResultCode resultCode,String msg){
        this.resultCode = resultCode;
        this.msg = msg;
    }


}
