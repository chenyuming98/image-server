package com.wnn.system.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wnn.system.frame.advice.GlobalException;
import com.wnn.system.common.utils.IdWorker;
import com.wnn.system.domain.system.Dictionary;
import com.wnn.system.domain.base.ResultCode;
import com.wnn.system.mapper.DictionaryMapper;
import com.wnn.system.vo.DictionaryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Transient;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 系统设置-字典管理模块
 */
@Service
public class DictionaryService {

    @Autowired
    private DictionaryMapper dictionaryMapper;

    @Autowired
    private IdWorker idWorker;

    private final String PARENT_TREE = "0";

    /**
     *  字典列表查询
     */
    public PageInfo<DictionaryVo> findDictionaryList(Integer page, Integer limit) {
        PageHelper.startPage(page,limit);
        List<DictionaryVo> dictionaryParentList = dictionaryMapper.selectDictionaryParentList();

        List<DictionaryVo> dictionaryList = dictionaryMapper.selectDictionaryList();

        List< DictionaryVo > dictionaryChildList = dictionaryList.stream().filter(d -> !PARENT_TREE.equals(d.getDictionaryParentId())).collect(Collectors.toList());
        for (DictionaryVo pd:dictionaryParentList ) {
            String pName = pd.getDictionaryName();
            String pId = pd.getDictionaryId();
            pd.setIsOneMenuInfo(true);
            ArrayList<DictionaryVo> childList = new ArrayList<>();
            for (DictionaryVo cd:dictionaryChildList ) {
                if (cd.getDictionaryParentId().equals(pId)){
                    cd.setIsOneMenuInfo(false);
                    childList.add(cd);
                }
            }
            pd.setChildren(childList);
        }
        return new PageInfo<>(dictionaryParentList);
    }

    /**
     *  字典按照
     */
    public String findDictionaryByDbFiledAndValue(String code ,String value){
        return dictionaryMapper.findDictionaryByDbFiledAndValue(code, value);
    }

    /**
     *  新增字典
     */
    @Transient
    public void addDictionary(Dictionary dictionary ) {
        if (StringUtils.isEmpty(dictionary .getDictionaryName())){
            throw  new GlobalException(ResultCode.NULL);
        }
        String dictionaryId = idWorker.nextId() ;
        dictionary.setDictionaryId(dictionaryId);
        dictionary.setSystemCode(0);
        dictionaryMapper.insert( dictionary );
    }


    /**
     *  保存字典,同时分配权限
     */
    @Transient
    public void saveDictionary(String id, Dictionary dictionary) {
        dictionary.setDictionaryId(id);
        Dictionary searchVo = new Dictionary();
        searchVo.setDictionaryId(id);
        Dictionary dictionaryDB = dictionaryMapper.selectOne(searchVo);
        if (dictionaryDB==null) {
         throw new GlobalException(ResultCode.SYSTEM_ERROR);
        }
        if (PARENT_TREE.equals(dictionaryDB.getDictionaryParentId())){
            if ( !dictionaryDB.getDictionaryCode() .equals(dictionary.getDictionaryCode())){
                Example example = new Example(Dictionary.class);
                example.createCriteria().andEqualTo("dictionaryParentId",dictionaryDB.getDictionaryId());
                List<Dictionary> cList = dictionaryMapper.selectByExample(example);
                for (Dictionary cd:cList
                     ) {
                    cd.setDictionaryCode(dictionary.getDictionaryCode());
                    dictionaryMapper.updateByPrimaryKey(cd);
                }
            }
        }
        dictionaryMapper.updateByPrimaryKeySelective(dictionary);
    }



    /**
     * 批量删除字典
     */
    public int batchDeleteDictionaryByIds(String ids) {
        return  dictionaryMapper.deleteByIds(ids);
    }


    /**
     *  字典父级列表查询,用于添加、编辑的下拉框
     */
    public List<Dictionary> findDictionaryAllParentList() {
        Example example = new Example(Dictionary.class);
        example.createCriteria().andEqualTo("dictionaryParentId",PARENT_TREE);
        return dictionaryMapper.selectByExample(example);

    }

    /**
     *  根据字典code获取字典列表
     */
    public List<Dictionary> findCodeList(String code) {
        Example example = new Example(Dictionary.class);
        example.createCriteria().andEqualTo("dictionaryCode",code);
        Dictionary dictionary = new Dictionary();
        dictionary.setDictionaryId("");
        dictionary.setDictionaryValue("请选择");
        ArrayList<Dictionary> list = new ArrayList<>();
        list.add(dictionary);
        List<Dictionary> dictionaryList = dictionaryMapper.selectByExample(example);
        if ( !dictionaryList.isEmpty()){
            List<Dictionary> collect = dictionaryList.stream().filter(l -> !PARENT_TREE.equals(l.getDictionaryParentId())).collect(Collectors.toList());
            list.addAll(collect);
        }
        return list;

    }
}
