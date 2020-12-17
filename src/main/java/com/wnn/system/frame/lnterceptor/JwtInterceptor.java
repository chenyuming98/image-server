package com.wnn.system.frame.lnterceptor;


import com.wnn.system.domain.base.ResultCode;
import com.wnn.system.frame.advice.GlobalException;
import com.wnn.system.common.utils.JwtUtils;
import com.wnn.system.common.utils.MyStringUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 拦截器-处理业务拦截
 */
@Component
public class JwtInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private JwtUtils jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request
                , HttpServletResponse response, Object handler) throws GlobalException {
        String authorization = request.getHeader("Authorization");
        if (!MyStringUtil.isNullOrEmpty(authorization)){
            if (authorization.startsWith("Bearer ")){
                String token = authorization.substring(7);
                try {
                    Claims claims = jwtUtil.parseJwt(token);
                    if (claims!=null){
                        request.setAttribute("user_claims",claims);
                        return true;
                    }
                } catch (RuntimeException e) {
                    throw new GlobalException(ResultCode.PLEASE_LOGIN);
                }
            }
        }
        throw new GlobalException(ResultCode.PLEASE_LOGIN);
    }
}