package com.wnn.system.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *  类描述:  字典注解
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Dict {

    /**
     * 方法描述:  数据dataSource，Code的意思
     * @return 返回类型： String
     */
    String dicDataSource();

    /**
     * 方法描述:  这是返回后Put到josn中的文本 key 值
     * @return 返回类型： String
     */
    String dicText() default "";



}
