package com.wnn.system.mapper;

import com.wnn.system.common.base.dao.IBaseDao;

import com.wnn.system.domain.image.Svm;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.BaseMapper;
import tk.mybatis.mapper.common.ExampleMapper;
import tk.mybatis.mapper.common.MySqlMapper;


/**
* Created by Mybatis Generator on 2020/03/15
*/
public interface SvmMapper extends IBaseDao<Svm>, ExampleMapper<Svm> {


    Svm selectLastData();


}

