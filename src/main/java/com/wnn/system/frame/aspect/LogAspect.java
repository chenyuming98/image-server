package com.wnn.system.frame.aspect;

import com.wnn.system.domain.base.BaseResult;
import com.wnn.system.domain.base.ResultCode;
import com.wnn.system.domain.system.Syslog;
import com.wnn.system.frame.advice.GlobalException;
import com.wnn.system.common.annotation.SysLog;
import com.wnn.system.common.utils.IdWorker;
import com.wnn.system.common.utils.ThrowableUtils;
import com.wnn.system.mapper.SysLogMapper;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.LinkedHashMap;

/**
 * 日志切面类
 * @author  wnn
 *
 */
@Aspect
@Component
@Slf4j
public class LogAspect {

    @Autowired
    private SysLogMapper syslogMapper;

    @Autowired
    private IdWorker idWorker;

    private final static String SUCCESS = "成功";
    private final static String FAILURE = "失败";

    /**
     * 设置切入点：这里直接拦截被@RestController注解的类
     */
    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void pointcut() {

    }

    /**
     * 切面方法,记录日志
     * @return
     * @throws Throwable
     */
    @Around("pointcut()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Boolean isExecute =false;
        //堆栈错误信息
        String errorInfo="";
        Date createTime = new Date();
        Signature signature = joinPoint.getSignature();
        if(!(signature instanceof MethodSignature)) {
            throw new IllegalArgumentException("暂不支持非方法注解");
        }
        //调用实际方法
        Object object = null;
        //获取执行的方法
        MethodSignature methodSign = (MethodSignature) signature;
        Method method = methodSign.getMethod();
        //判断是否包含了 无需记录日志的方法
        SysLog logAnno = AnnotationUtils.getAnnotation(method, SysLog.class);
        //调用目标执行可能会出现异常
        try {
            object = joinPoint.proceed();
            if (logAnno==null){
                return object;
            }
            isExecute =true;
        } catch (Exception eg) {
            isExecute =true;
            if ( eg instanceof GlobalException){
                GlobalException g = (GlobalException) eg;
                ResultCode resultCode = g.getResultCode();
                object = new BaseResult(resultCode.getCode(),g.getMsg());
                eg.printStackTrace();
                System.out.println("自定义异常信息："+g.getMsg());
//                throw new GlobalException(resultCode,g.getMsg());
            }else {
                errorInfo = ThrowableUtils.throwableToString(eg);
                object = new BaseResult(ResultCode.SYSTEM_ERROR,errorInfo);
                throw new GlobalException(ResultCode.SYSTEM_ERROR);
            }
        } finally {
            //如果没有添加AOP注解 会直接导致报错，只有添加注解才记录
            if (isExecute){
                //利用RequestContextHolder获取request对象
                ServletRequestAttributes requestAttr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
                HttpServletRequest request = requestAttr.getRequest();
                Claims claims = (Claims) requestAttr.getRequest().getAttribute("user_claims");
                String username ;
                String userId ;
                //第一次用户登录，作用域没有user_claims，需要特殊处理
                if (claims==null){
                    username = request.getParameter("username");
                    userId = "null";
                }else {
                    LinkedHashMap<String, String> userInfo = (LinkedHashMap<String, String>) claims.get("userInfo");
                    username =  userInfo.get("username");
                    userId =  userInfo.get("userId");
                }
                //获取访问  1.路径 url
                String url = requestAttr.getRequest().getRequestURI();
                //获取方法名 2.方法名 methodName
                String methodName = joinPoint.getSignature().getName();
                //访问目标方法的参数 可动态改变参数值 3.参数 args
                Object[] args = joinPoint.getArgs();
                String postArgs = "";
                for ( Object  a:args
                     ) {
                    if (a instanceof String){
                        postArgs += a+"   ;   ";
                    }
                }
//                String postArgs = Arrays.toString(args);
                //可能在反向代理请求进来时，获取的IP存在不正确行 这里直接摘抄一段来自网上获取ip的代码
                // 4.IP ipAddr
                String ipAddr = getIpAddr(requestAttr.getRequest());
                BaseResult baseResult = (BaseResult) object;
                Date endTime = new Date();
                long consumingTime = createTime.getTime() - createTime.getTime();
                String result = FAILURE;
                if ( ResultCode.SUCCESS.getCode() == baseResult.getCode()) {
                    result = SUCCESS;
                }
                Syslog syslog = new Syslog(idWorker.nextId(),userId,username, logAnno != null ? logAnno.value() : "注解未知",result
                        ,ipAddr,url,methodName, postArgs,consumingTime,createTime,endTime,baseResult.getMsg(),errorInfo);
                syslogMapper.insert(syslog);
            }
        }
        return object;
    }

    /**
     * 转至：https://my.oschina.net/u/994081/blog/185982
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ipAddress = null;
        try {
            ipAddress = request.getHeader("x-forwarded-for");
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getRemoteAddr();
                if (ipAddress.equals("127.0.0.1")) {
                    // 根据网卡取本机配置的IP
                    InetAddress inet = null;
                    try {
                        inet = InetAddress.getLocalHost();
                    } catch (UnknownHostException e) {
                        log.error("获取ip异常：{}" ,e.getMessage());
                        e.printStackTrace();
                    }
                    ipAddress = inet.getHostAddress();
                }
            }
            // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
            if (ipAddress != null && ipAddress.length() > 15) { // "***.***.***.***".length()
                                                                // = 15
                if (ipAddress.indexOf(",") > 0) {
                    ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
                }
            }
        } catch (Exception e) {
            ipAddress = "";
        }
        return ipAddress;
    }
}