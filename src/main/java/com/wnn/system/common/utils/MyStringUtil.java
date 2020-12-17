package com.wnn.system.common.utils;

import java.util.Calendar;

/**
 * @Auther: wn
 * @Date: 2019/11/1 11:46
 * @Description:    自定义的字符串处理操作类
 */
public class MyStringUtil {

    /**
     * 判断一个字符串为Null 且 为空字符
     * @param   str   字符串
     * @return   boolean
     */
    public static boolean isNullOrEmpty(String str) {

        if (str ==null){
            return  true;
        }else {
            if (str.length()<1){
                return true;
            }else {
                return str.trim().length() < 1;
            }
        }

    }

    /**
     * 判断一个字符串不为Null 且 不为空字符
     * @param   str   字符串
     * @return    boolean
     */
    public static boolean notIsNullOrEmpty(String str) {

        if (str ==null){
            return  false;
        }else {
            if (str.length()<1){
                return false;
            }else {
                boolean b = str.trim().length() >0;

                return b;
            }
        }

    }


    /**
     * 获取当前时间到第二天的0点的剩余毫秒
     * @return
     */
    public static long getRemainderSeconds() {
        Calendar ca = Calendar.getInstance();
        //失效的时间
        ca.add(Calendar.DAY_OF_YEAR,1);
        ca.set(Calendar.HOUR_OF_DAY, 0);
        ca.set(Calendar.MINUTE, 0);
        ca.set(Calendar.SECOND, 0);
        ca.set(Calendar.MILLISECOND,0);
        long remainderSeconds= (ca.getTimeInMillis() - System.currentTimeMillis())/1000; //获取剩余时间
        System.out.println("setRedis time is " + remainderSeconds);
        return remainderSeconds;
    }

}
