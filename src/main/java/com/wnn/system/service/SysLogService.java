package com.wnn.system.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import com.wnn.system.domain.system.Syslog;
import com.wnn.system.mapper.SysLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class SysLogService {

    @Autowired
    private SysLogMapper sysLogMapper;

    /**
     *  日志列表查询
     */
    public  PageInfo<Syslog> findSysLogList(String username,String result,String ipAddr
                                    ,String createTime,String createTimeEnd,Integer page, Integer limit) {
        PageHelper.startPage(page,limit);
        List<Syslog> sysLogs = sysLogMapper.selectSysLogList( username, result, ipAddr
                                                        , createTime, createTimeEnd);
        return new PageInfo<>(sysLogs);
    }

    /**
     * 批量删除日志
     */
    public  int batchDeleteSysLogByIds(String ids) {
        return  sysLogMapper.deleteByIds(ids);
    }
}
