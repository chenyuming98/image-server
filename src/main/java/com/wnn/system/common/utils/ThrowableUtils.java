package com.wnn.system.common.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * [Project]:moy-gradle-project  <br/>
 * [Email]:moy25@foxmail.com  <br/>
 * [Date]:2018/4/8  <br/>
 * [Description]:  <br/>
 *
 * @author YeXiangYang
 */
public abstract class ThrowableUtils {

    /**
     * 将异常信息转化为字符串
     *
     * @param throwable 异常对象
     * @return 异常信息字符串
     */
    public static String throwableToString(Throwable throwable) {
        try (
            StringWriter stringWriter = new StringWriter();
            PrintWriter writer = new PrintWriter(stringWriter)) {
            throwable.printStackTrace(writer);
            StringBuffer buffer = stringWriter.getBuffer();
            return buffer.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}