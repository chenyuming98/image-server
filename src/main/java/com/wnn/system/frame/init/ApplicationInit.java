package com.wnn.system.frame.init;
 

import com.wnn.system.common.utils.DictionaryUtil;
import com.wnn.system.common.utils.UtilConstants;
import com.wnn.system.mapper.DictionaryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.File;


/**
 * @Description springboot项目启动 完毕加载自定义一些任务
 */
@Component
public class ApplicationInit   implements ApplicationRunner {

    @Autowired
    private DictionaryMapper dictionaryMapper;

    @Override
    public void run(ApplicationArguments applicationArguments){
        DictionaryUtil dictionaryUtil = new DictionaryUtil();
        dictionaryUtil.initDictionary( dictionaryMapper.selectAll());
        UtilConstants.ProjectPath.PATH = System.getProperty("user.dir") + "\\file\\";
        UtilConstants.ProjectPath.ROOT_PATH = System.getProperty("user.dir");
        System.out.println("初始化字典容器数据成功！");
    }
}