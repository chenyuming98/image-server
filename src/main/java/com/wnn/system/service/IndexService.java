package com.wnn.system.service;

import com.wnn.system.common.utils.CompletionDateUtils;
import com.wnn.system.common.utils.ImageUtil;
import com.wnn.system.domain.system.Syslog;
import com.wnn.system.mapper.SysLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class IndexService {


    @Autowired
    private SysLogMapper sysLogMapper;

    /**
     * 获取标签信息
     */
    public  HashMap<String, Object> getLabelInfo() {
        Date date = new Date();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");//显示2017-10-27格式
        String formatData = sdf.format(date);
        HashMap<String, Object> resultMap = new HashMap<>();
        ImageUtil imageUtil = new ImageUtil();
        HashMap<String, List<String>> imageFilePath = imageUtil.getImageFilePath("/train");
        HashMap<String, List<String>> matchImageFilePath = imageUtil.getImageFilePath("/match");

        int totalTrainSize = 0; //总训练图片数
        int todayAddTotal = 0; //今日新增训练图片数
        int trainClassTotal = 0;//训练集分类

        int totalMatchSize = 0; //总测试集图片数
        int todayMatchAddTotal = 0; //今日新增测试集图片数
        int matchClassTotal = 0;//测试集集分类

        int visitTotal = 0;//总访问次数
        long todayVisitTotal = 0;//今天访问次数
        int classCount = 0;//总访问次数
        int trainCount = 0;//今天访问次数

        int errorHttpTotalCount = 0;//总失败访问次数
        int errorHttpTodayCount = 0;//今日失败访问次数
        int errorHttpSevenDayCount = 0;//近七天错误数
        trainClassTotal = imageFilePath.size();
        matchClassTotal = matchImageFilePath.size();
        for (HashMap.Entry<String,  List<String>> entry:imageFilePath.entrySet()){
            List<String> value = entry.getValue();
            for (String s:value
                 ) {
                File file = new File(s);
                String creationTime = getCreationTime(file);
                if (formatData.equals(creationTime)){
                    todayAddTotal = todayAddTotal+1;
                }
            }
            int size = value.size();
            totalTrainSize = size + totalTrainSize;
        }
        for (HashMap.Entry<String,  List<String>> entry:matchImageFilePath.entrySet()){
            List<String> value = entry.getValue();
            for (String s:value
            ) {
                File file = new File(s);
                String creationTime = getCreationTime(file);
                if (formatData.equals(creationTime)){
                    todayMatchAddTotal = todayMatchAddTotal+1;
                }
            }
            int size = value.size();
            totalMatchSize = size + totalMatchSize;
        }


        //lab-3 今日访问次数
        Syslog syslog = new Syslog();
        List<Syslog> syslogs = sysLogMapper.select(syslog);
        visitTotal = syslogs.size();
        LocalDateTime now = LocalDateTime.now(); //今天
        LocalDateTime after = LocalDateTime.now().plusDays(1); //今天+1
        LocalDateTime sevenDay = LocalDateTime.now().plusDays(6); //前7天
        DateTimeFormatter sdfs = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        List<HashMap<String, Object>> hashMaps = sysLogMapper.selectList(sdfs.format(now),sdfs.format(after));
        if (!hashMaps.isEmpty()){
            HashMap<String, Object> map = hashMaps.get(0);
            todayVisitTotal =  (Long) map.get("total");
        }
        List<HashMap<String, Object>> uploadList = sysLogMapper.selectHttpList("/image/uploadImages",null, sdfs.format(now), sdfs.format(after));
        List<HashMap<String, Object>> trainSvmList = sysLogMapper.selectHttpList("/image/trainSvm",null,sdfs.format(now), sdfs.format(after));
        classCount = uploadList.size(); //总访问次数
        trainCount =trainSvmList.size(); //今天访问次数

        List<HashMap<String, Object>> errorSevenDayList = sysLogMapper.selectHttpList(null,"失败", sdfs.format(now),sdfs.format(sevenDay));
        List<HashMap<String, Object>> errorHttpList = sysLogMapper.selectHttpList(null,"失败", sdfs.format(now), sdfs.format(after));
        List<HashMap<String, Object>> errorTodayHttpList = sysLogMapper.selectHttpList(null,"失败", null, null);
        errorHttpTodayCount = errorHttpList.size();
        errorHttpTotalCount = errorTodayHttpList.size();
        errorHttpSevenDayCount = errorSevenDayList.size();

        resultMap.put("trainTotal",totalTrainSize);
        resultMap.put("todayAddTotal",todayAddTotal);
        resultMap.put("trainClassTotal",trainClassTotal);//训练集分类

        resultMap.put("totalMatchSize",totalMatchSize);
        resultMap.put("todayMatchAddTotal",todayMatchAddTotal);
        resultMap.put("matchClassTotal",matchClassTotal);//测试集集分类

        resultMap.put("visitTotal",visitTotal);
        resultMap.put("todayVisitTotal",todayVisitTotal);
        resultMap.put("classCount",classCount);
        resultMap.put("trainCount",trainCount);

        resultMap.put("errorHttpTotalCount",errorHttpTotalCount);
        resultMap.put("errorHttpTodayCount",errorHttpTodayCount);
        resultMap.put("errorHttpSevenDayCount",errorHttpSevenDayCount);
        return resultMap;
    }


    /**
     * 报表统计
     * @return
     */
    public HashMap<String, Object> getStaticInfo() {
        HashMap<String, Object> resultMap = new HashMap<>();
        LocalDateTime after = LocalDateTime.now().minusDays(13);
        LocalDateTime now = LocalDateTime.now().plusDays(1);
        DateTimeFormatter sdf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        List<HashMap<String, Object>> hashMaps = sysLogMapper.selectList(sdf.format(after),sdf.format(now));
        HashMap<String, Object> statisticsVisitMap = CompletionDateUtils.dealData(hashMaps,after, now);
        resultMap.put("statisticsVisitMap",statisticsVisitMap);
        return resultMap;
    }


    /**
     * 获取文件创建时间
     */
    public static String getCreationTime(File file) {
        if (file == null) {
            return null;
        }
        BasicFileAttributes attr = null;
        try {
            Path path =  file.toPath();
            attr = Files.readAttributes(path, BasicFileAttributes.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 创建时间
        Instant instant = attr.creationTime().toInstant();
        // 更新时间
//        Instant instant = attr.lastModifiedTime().toInstant();
        // 上次访问时间
//        Instant instant = attr.lastAccessTime().toInstant();
        String format = DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneId.systemDefault()).format(instant);
        return format;
    }


}
