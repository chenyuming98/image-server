package com.wnn.system.domain.base;


import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 返回信息枚举类
 */
@Getter
@NoArgsConstructor
public enum ResultCode {

    OK(200,"操作成功！"),
    SUCCESS(200),
    FALSE(500),
    ERROR(400,"参数错误"),
    NULL(400),
    PLEASE_LOGIN(500,"请先登录！"),
    SYSTEM_ERROR(500,"系统错误");

    int code;
    String msg;


    ResultCode(int code, String msg){
        this.code = code;
        this.msg = msg;
    }

    ResultCode( int code){
        this.code = code;
    }
}
