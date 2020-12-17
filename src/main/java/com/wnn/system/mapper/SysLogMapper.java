package com.wnn.system.mapper;

import com.wnn.system.common.base.dao.IBaseDao;
import com.wnn.system.domain.system.Syslog;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

/**
* Created by Mybatis Generator on 2020/05/16
*/
public interface SysLogMapper extends IBaseDao<Syslog> {

    /**
     * 日志列表查询
     * @param username
     * @param result
     * @param ipAddr
     * @param createTime
     * @param createTimeEnd
     * @return
     */
    List<Syslog> selectSysLogList(@Param("username") String username
            , @Param("result") String result, @Param("ipAddr") String ipAddr
            , @Param("createTime") String createTime, @Param("createTimeEnd") String createTimeEnd);


    /**
     * @param small 7天前日期
     * @param big 当前日期
     * @return
     */
    List<HashMap<String,Object>> selectList(@Param("small") String small, @Param("big") String big) ;

    List<HashMap<String,Object>> selectHttpList(@Param("url") String url, @Param("result") String result, @Param("small") String small, @Param("big") String big);
}