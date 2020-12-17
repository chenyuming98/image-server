package com.wnn.system.common.utils;

import com.wnn.system.domain.system.Dictionary;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 字典容器工具类
 */
public  class DictionaryUtil{


    private  static ConcurrentHashMap<String, String>
            dictionaryContainer = new ConcurrentHashMap<String,String>();

    private  static ConcurrentHashMap<String, Dictionary>
            dictionaryToKey= new ConcurrentHashMap<String,Dictionary>();

    public void initDictionary(List<Dictionary> list){
        list.forEach(l -> {
            String name = l.getDictionaryCode() + "+" + l.getDictionaryValue();
            dictionaryContainer.put(name,l.getDictionaryName());
        });
        list.forEach(l -> { dictionaryToKey.put(l.getDictionaryId(),l);});
    }

    public static ConcurrentHashMap<String, String> getContainer(){
        return dictionaryContainer;
    }

    public static Dictionary getContainerByKey(String key){
        return dictionaryToKey.get(key);
    }
}
