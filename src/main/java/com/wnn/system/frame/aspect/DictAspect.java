package com.wnn.system.frame.aspect;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wnn.system.common.annotation.Dict;
import com.wnn.system.common.utils.DictionaryUtil;
import com.wnn.system.common.utils.MyStringUtil;
import com.wnn.system.common.utils.ObjConvertUtils;
import com.wnn.system.domain.base.BaseResult;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Aspect
@Component
@Slf4j
public class DictAspect {
    private static String DICT_TEXT_SUFFIX = "_dictText";



    /**
     * 定义切点Pointcut 拦截所有对服务器的请求
     */
    @Pointcut("execution( * com.wnn..*.*Controller.*(..))")
    public void executeService() {
    }

    /**
     * 这是触发 executeService 的时候会执行的，在环绕通知中目标对象方法被调用后的结果进行再处理
     * @throws Throwable
     */
    @Around("executeService()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        //这是定义开始事件
        long time1 = System.currentTimeMillis();
        //这是方法并获取返回结果
        Object result =  pjp.proceed();
        //这是获取到 结束时间
        long time2 = System.currentTimeMillis();
        log.debug("获取JSON数据 耗时：" + (time2 - time1) + "ms");
        //解析开始时间
        long start = System.currentTimeMillis();
        //开始解析（翻译字段内部的值凡是打了 @Dict 这玩意的都会被翻译）
        this.parseDictText(result);
        //解析结束时间
        long end = System.currentTimeMillis();
        log.debug("解析注入JSON数据  耗时" + (end - start) + "ms");
        return result;
    }

    /**
     * 指定拦截器规则；也可直接使用within(@org.springframework.web.bind.annotation.RestController *)
     * 这样简单点 可以通用 异常对象
     * @param
     */
//    @AfterThrowing(pointcut="executeService()",throwing="e")
//    public void afterThrowable(Throwable e) {
//        log.error("切面发生了异常：", e);
//        throw new GlobalException(ResultCode.FALSE,e+"");
//    }

    /**
     * 本方法针对返回对象为Result 的PageUtils的分页列表数据进行动态字典注入
     * 字典注入实现 通过对实体类添加注解@dict 来标识需要的字典内容,字典分为单字典dataSource即可
     * 示例为Student
     *  字段为stu_sex 添加了注解@Dict(dicDataSource = "stu_sex") 会在字典服务立马查出来对应的text 然后在请求list的时候将这个字典text，已字段名称加_dictText形式返回到前端
     * 例输入当前返回值的就会多出一个stu_sex_dictText字段
     * {
     * stu_sex:1,
     * stu_sex_dictText:"男"
     * }
     * 前端直接取值sext_dictText在table里面无需再进行前端的字典转换了
     * customRender:function (text) {
     * if(text==1){
     * return "男";
     * }else if(text==2){
     * return "女";
     * }else{
     * return text;
     * }
     * }
     * 目前vue是这么进行字典渲染到table上的多了就很麻烦了 这个直接在服务端渲染完成前端可以直接用
     *
     * @param result
     */
    private void parseDictText(Object result) {
        if (result instanceof BaseResult) {
            List<JSONObject> items = new ArrayList<>();
            Object object = ((BaseResult) result).getData();
            if (object instanceof List) {
                List resultList = (List) ((BaseResult) result).getData();
                if (resultList == null || resultList.isEmpty()) {
                    return;
                }
                //循环查找出来的数据
                for (Object record : (List) ((BaseResult) result).getData()) {
                    ObjectMapper mapper = new ObjectMapper();
                    String json = "{}";
                    try {
                        //解决@JsonFormat注解解析不了的问题详见SysAnnouncement类的@JsonFormat
                        json = mapper.writeValueAsString(record);
                    } catch (JsonProcessingException e) {
                        log.error("json解析失败" + e.getMessage(), e);
                    }
                    JSONObject item = JSONObject.parseObject(json);

                    //update-begin--Author:scott -- Date:20190603 ----for：解决继承实体字段无法翻译问题------
                    //for (Field field : record.getClass().getDeclaredFields()) {
                    for (Field field : ObjConvertUtils.getAllFields(record)) {
                        //update-end--Author:scott  -- Date:20190603 ----for：解决继承实体字段无法翻译问题------
                        if (field.getAnnotation(Dict.class) != null) {
                            String code = field.getAnnotation(Dict.class).dicDataSource();
                            String text = field.getAnnotation(Dict.class).dicText();
                            //获取当前带翻译的值
                            String value = String.valueOf(item.get(field.getName()));
                            //翻译字典值对应的txt
                            String textValue = translateDictValue(code, value);
                            //  DICT_TEXT_SUFFIX的值为，是默认值：
                            // public static final String DICT_TEXT_SUFFIX = "_dictText";
                            log.debug(" 字典Val : " + textValue);
                            log.debug(" __翻译字典字段__ " + field.getName() + DICT_TEXT_SUFFIX + "： " + textValue);
                            //如果给了文本名
                            if (!StringUtils.isEmpty(text)) {
                                item.put(text, textValue);
                            } else {
                                //走默认策略
                                item.put(field.getName() + DICT_TEXT_SUFFIX, textValue);
                            }
                        }
                    }
                    items.add(item);
                }
                ((BaseResult) result).setData(items);

            }
        }
    }

    /**
     * 翻译字典文本
     * code+value 能够组成唯一翻译索引
     * @param code 代号
     * @param value 参数
     * @return
     */
    private String translateDictValue(String code, String value) {
        if (MyStringUtil.isNullOrEmpty(code)){
            return "unknown";
        }
        ConcurrentHashMap<String, String> dictionaryContainer = DictionaryUtil.getContainer();
        String searchValue = dictionaryContainer.get(code + "+" + value);
        if (MyStringUtil.isNullOrEmpty(searchValue)){
            return "unknown";
        }else {
            return searchValue;
        }
    }
}
