package com.wnn.system.frame.config;

import com.wnn.system.common.utils.UtilConstants;
import com.wnn.system.frame.lnterceptor.JwtInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * JWT拦截器配置类，设置拦截路径和不拦截路径
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private JwtInterceptor jwtInterceptor;


    String[] path = new String[]{"/system/user/login"
            ,"/swagger-ui.html","/swagger-resources/**","/webjars/**"
            , "/v2/**", "/swagger-ui.html/**","/imageLoad/**","/imageLoad/cache/"};

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(path);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String  path = System.getProperty("user.dir") + "\\file\\";
        String cachePath = System.getProperty("user.dir") +UtilConstants.ProjectPath.CACHE_PATH+"\\"  ;
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");

        registry.addResourceHandler(UtilConstants.ProjectPath.VIRTUAL_PATH+ "/**")
                .addResourceLocations( "file:/" + path);
        registry.addResourceHandler(UtilConstants.ProjectPath.VIRTUAL_PATH_CACHE+ "/**")
                .addResourceLocations( "file:/" + cachePath);
    }


}
