package com.wnn.system.config;


import com.wnn.system.domain.image.Svm;
import org.springframework.scheduling.annotation.Scheduled;
import java.util.LinkedList;
import java.util.Queue;

/**
 * 定时任务
 */
//@Component
public class TasksTimer {


    public static Queue<Svm> queue = new LinkedList<Svm>();


    /**
     *  fixedDelay上一次执行完毕 10S后再执行
     */
    @Scheduled(fixedDelay = 300)
    public void runTimer() {
        Svm svmPojo = queue.poll();
        if (svmPojo!=null){ queue.add(svmPojo);

        }
    }




}