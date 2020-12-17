package com.wnn.system.common.base.controller;

import com.wnn.system.common.utils.JwtUtils;
import com.wnn.system.common.utils.MyStringUtil;
import com.wnn.system.domain.system.vo.UserVo;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;

/**
 * 控制层基类
 * @author asus
 */
@Controller
public class BaseController {


    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected UserVo userObject;

    @Autowired
    private JwtUtils jwtUtils;


    @ModelAttribute
    public void setResAnReq(HttpServletRequest request,HttpServletResponse response) {
        this.request = request;
        this.response = response;
        String authorization = request.getHeader("Authorization");
        if (!MyStringUtil.isNullOrEmpty(authorization) && authorization.startsWith("Bearer ")){
            String token = authorization.substring(7);
            Claims claims = jwtUtils.parseJwt(token);
            LinkedHashMap userMap = (LinkedHashMap) claims.get("userInfo");
            UserVo user = new UserVo();
            user.setUserId((String)userMap.get("userId") );
            user.setUsername((String)userMap.get("username") );
            user.setEmployeeId((String)userMap.get("employeeId") );
            this.userObject = user;
        }
    }

}
