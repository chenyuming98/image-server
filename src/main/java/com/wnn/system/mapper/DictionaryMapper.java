package com.wnn.system.mapper;

import com.wnn.system.common.base.dao.IBaseDao;
import com.wnn.system.domain.system.Dictionary;
import com.wnn.system.vo.DictionaryVo;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.ExampleMapper;

import java.util.List;


public interface DictionaryMapper extends IBaseDao<Dictionary>, ExampleMapper<Dictionary> {


    /**
     *   查询字典 返回包装类列表
     */
    List<DictionaryVo> selectDictionaryList();

    /**
     * 查询字典一级菜单 返回包装类列表
     */
    List<DictionaryVo> selectDictionaryParentList();

    String findDictionaryByDbFiledAndValue(@Param("code") String code, @Param("value") String value);
}