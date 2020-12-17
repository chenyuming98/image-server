package com.wnn.system;

import com.wnn.system.common.utils.CaptchaUtil;
import com.wnn.system.common.utils.ImageUtil;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.ml.Ml;
import org.opencv.ml.SVM;
import org.opencv.objdetect.HOGDescriptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;
import tk.mybatis.spring.annotation.MapperScan;

import java.util.HashMap;
import java.util.List;

/**
 * 用户系统项目程序启动类
 * scanBasePackages微服务扫描配置类范围包含 com.wnn.* 类下包范围
 */
@SpringBootApplication(scanBasePackages = "com.wnn.*")
@MapperScan("com.wnn.system.mapper")
@EnableAspectJAutoProxy
//@EnableScheduling
public class SystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(SystemApplication.class, args);
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        System.out.println("system web server start success !");
    }


    @Bean
    public CaptchaUtil captchaUtil(){
        return new CaptchaUtil();
    }


}
