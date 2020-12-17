package com.wnn.system.common.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 日志注解类
 * @author wnn
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})//只能在方法上使用此注解
public @interface SysLog {
    /**
     * 日志描述，这里使用了@AliasFor 别名。spring提供的
     * @return
     */
    @AliasFor("desc")
    String value() default "";
    
    /**
     * 日志描述
     * @return
     */
    @AliasFor("value")
    String desc() default "";
    
    /**
     * 是否不记录日志
     * @return
     */
    boolean ignore() default false;
}