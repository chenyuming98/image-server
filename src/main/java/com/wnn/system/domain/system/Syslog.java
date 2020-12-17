package com.wnn.system.domain.system;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Id;
import java.util.Date;

/**
* Created by Mybatis Generator on 2020/05/16
*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Syslog {
    @Id
    private String logId;

    /**
     * 用户主键
     */
    private String userId;

    /**
     * 操作用户名
     */
    private String username;

    /**
     * 操作
     */
    private String operate;

    /**
     * 结果
     */
    private String result;

    /**
     * 用户ip地址
     */
    private String ipAddr;

    /**
     * 请求url
     */
    private String url;

    /**
     * 方法名
     */
    private String method;

    /**
     * 请求参数
     */
    private String args;

    /**
     * 耗时
     */
    private Long consumingTime;

    /**
     * 开始时间
     */
    private Date createTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 错误信息
     */
    private String error;

    /**
     * 错误堆栈信息
     */
    private String errorInfo;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getCreateTime() {
        return createTime;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getEndTime() {
        return endTime;
    }
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}