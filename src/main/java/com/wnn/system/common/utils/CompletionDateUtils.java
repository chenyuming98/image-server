package com.wnn.system.common.utils;

import org.apache.poi.ss.formula.functions.T;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
 
 
public final class CompletionDateUtils {
 
    /**
     * 隐藏构造方法.
     */
    private CompletionDateUtils() {
    }
 
    /**
     * 数据库查询出来的统计数据有时候日期是不连续的.
     * 但是前端展示要补全缺失的日期.
     * 此方法返回一个给定日期期间的所有日期字符串列表.
     * 具体在业务逻辑中去判断补全.
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return
     */
    public static List<String> completionDate(
            LocalDateTime startDate,
            LocalDateTime endDate) {
        //日期格式化
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        List<String> dateList = new ArrayList<>();
        //遍历给定的日期期间的每一天
        for (int i = 0; !Duration.between(startDate.plusDays(i), endDate).isNegative(); i++) {
            //添加日期
            dateList.add(startDate.plusDays(i).format(formatter));
        }
        return dateList;
    }

    public static HashMap<String, Object>  dealData(List<HashMap<String, Object> > data,LocalDateTime lastDay,LocalDateTime nowDay){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        HashMap<String, Object> dataMap = new HashMap<>();
        for (HashMap<String, Object>  t:data
             ) {
            Timestamp creatTime = (Timestamp) t.get("createTime");
            dataMap.put(sdf.format(creatTime),t);
        }
        ArrayList<Object> nums = new ArrayList<>();
        List<String> dates = completionDate(lastDay,nowDay );
        for (String generateDay: dates) {
            HashMap<String, Object> isExit = (HashMap<String, Object>) dataMap.get(generateDay);

            if (isExit==null){
                nums.add(0);
            }else {
                Long total = (Long) isExit.get("total");
                nums.add(total);
            }
        }
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("day",dates);
        resultMap.put("data",nums);
        return resultMap;
    }


    /**
     * main.
     *
     * @param args
     */
    public static void main(String[] args) {


    }
}
 