package com.wnn.system.controller;

import com.github.pagehelper.PageInfo;
import com.wnn.system.common.base.controller.BaseController;
import com.wnn.system.domain.system.vo.UserVo;
import com.wnn.system.common.utils.CaptchaUtil;
import com.wnn.system.domain.base.BaseResult;
import com.wnn.system.domain.base.ResultCode;
import com.wnn.system.common.annotation.SysLog;
import com.wnn.system.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 系统设置-用户管理模块
 */
@RestController
@RequestMapping("/system/user")
@Api(description = "用户管理")
public class UserController extends BaseController {


    @Autowired
    private UserService userService;

    @Autowired
    private CaptchaUtil captchaUtil;

    public static final String KEY_CAPTCHA = "KEY_CAPTCHA";

    /**
     * 分页查询全部用户的信息，链表查询了角色信息
     */
    @GetMapping
    public BaseResult findUserList( @RequestParam(defaultValue = "1")Integer page,
                           @RequestParam(defaultValue = "10")Integer size){
        PageInfo<UserVo> pageInfo = userService.findUserList(page, size);
        return new BaseResult(ResultCode.OK, pageInfo);
    }

    /**
     * 用户登录
     */
    @SysLog("用户登录")
    @RequestMapping(value="/login",method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username",value = "用户名称",required = true,paramType = "form",dataType = "string"),
            @ApiImplicitParam(name = "userPassword",value = "密码",required = true,paramType = "form",dataType = "string")
    })
    public BaseResult login( String username, String userPassword ) {
        Map<String, Object>  map = userService.userLogin(username,userPassword );
        return new BaseResult(ResultCode.OK,map);
    }

    /**
     *  新增用户
     */
    @SysLog("新增用户")
    @PostMapping
    public BaseResult addUser(@RequestBody UserVo user) {
        userService.addUser(user);
        return BaseResult.ok();
    }


    /**
     * 更新用户
     */
    @SysLog("更新用户")
    @RequestMapping(value="/{id}",method = RequestMethod.PUT)
    public BaseResult editUser(@PathVariable(value = "id")String id,@RequestBody UserVo user) {
        userService.editUser(id,user);
        return BaseResult.ok();
    }

    /**
     * 删除单个用户
     */
    @SysLog("删除单个用户")
    @RequestMapping(value="/{id}",method = RequestMethod.DELETE)
    public BaseResult deleteUserById(@PathVariable(value = "id") String id) {
        userService.deleteUserById(id);
        return BaseResult.ok();
    }


    /**
     * 批量删除用户
     */
    @SysLog("批量删除用户")
    @RequestMapping(value="/deletes",method = RequestMethod.POST)
    public BaseResult deletes(String ids) {
        int i = userService.deletes(ids);
        if (i==0){ return BaseResult.failure(); }
        return BaseResult.ok();
    }

    /**
     *获取登录验证码
     */
//    @RequestMapping("/captcha.jpg")
//    public void getCaptcha() {
//        // 设置相应类型,告诉浏览器输出的内容为图片
//        response.setContentType("image/jpeg");
//        // 不缓存此内容
//        response.setHeader("Pragma", "No-cache");
//        response.setHeader("Cache-Control", "no-cache");
//        response.setDateHeader("Expire", 0);
//        try {
//            HttpSession session = request.getSession();
//            StringBuffer code = new StringBuffer();
//            BufferedImage image = captchaUtil.genRandomCodeImage(code);
//            session.removeAttribute(KEY_CAPTCHA);
//            session.setAttribute(KEY_CAPTCHA, code.toString());
//            // 将内存中的图片通过流动形式输出到客户端
//            ImageIO.write(image, "JPEG", response.getOutputStream());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * 用户员工头像上传
     * @param file 头像
     */
//    @PostMapping("/user/uploadImage/{userId}")
//    public BaseResult uploadImage(@PathVariable(value = "userId")String userId,MultipartFile file) {
//        String url = userService.uploadImage(userId,file);
//        return new BaseResult(ResultCode.OK ,url);
//    }


}
