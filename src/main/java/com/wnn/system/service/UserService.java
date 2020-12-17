package com.wnn.system.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wnn.system.domain.system.vo.UserVo;
import com.wnn.system.frame.advice.GlobalException;
import com.wnn.system.common.utils.IdWorker;
import com.wnn.system.common.utils.JwtUtils;
import com.wnn.system.common.utils.Md5Utils;
import com.wnn.system.common.utils.MyStringUtil;
import com.wnn.system.domain.system.*;
import com.wnn.system.domain.base.ResultCode;
import com.wnn.system.mapper.UserMapper;
import com.wnn.system.domain.system.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Transient;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 系统设置-用户管理模块
 */
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private JwtUtils jwtUtils;

//    @Autowired
//    private ThumbImageConfig thumbImageConfig;
//
//    @Autowired
//    private FastFileStorageClient storageClient;

    /**
     * 用户登录
     */
    public Map<String, Object>  userLogin(String username,String userPassword) {
        if (MyStringUtil.isNullOrEmpty(username) || MyStringUtil.isNullOrEmpty(userPassword) ){
            throw new GlobalException(ResultCode.FALSE,"用户名或者不能为空！");
        }
        String password = Md5Utils.myEncryptPassword(userPassword, username, 10);
        User user = userMapper.selectUserByUsername(username);
        if (user==null){
            throw new GlobalException(ResultCode.FALSE,"用户名或者密码错误！");
        }
        if (!password.equals(user.getUserPassword())){
            throw new GlobalException(ResultCode.FALSE,"用户名或者密码错误！");
        }
        Map<String, Object> tempMap = new HashMap<>();
        tempMap.put("userInfo",user);
        String jwtStr = jwtUtils.createJwt(user.getUserId(), user.getUsername(), tempMap);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("jwtStr",jwtStr);
        resultMap.put("jwtStrSwagger","Bearer "+jwtStr);
        resultMap.put("username",user.getUsername());
        return resultMap;
    }

    /**
     * 查询全部用户的信息，链表查询了角色信息
     */
    public PageInfo<UserVo> findUserList(Integer page, Integer limit) {
        PageHelper.startPage(page,limit);
        List<UserVo> users = userMapper.findUserList();
        users.forEach(u ->  u.setUserPassword(null) );
        return new PageInfo<>(users);
    }

    /**
     * 新增用户
     * @param user 用户对象
     * @return
     */
    @Transient
    public void addUser(UserVo user) {
        if (user.getUsername()==null){
            throw new GlobalException(ResultCode.NULL,"用户名为空");
        }
        String id = idWorker.nextId() ;
        // 获取一个Date对象
        Date date = new Date();
        Timestamp timeStamp = new Timestamp(date.getTime());
        user.setCreateTime(timeStamp);
        user.setUserPassword(Md5Utils.myEncryptPassword(user.getUserPassword(),user.getUsername(),10));
        user.setUserId(id);
        user.setUserStatus(0);
        userMapper.insert(user);
        this.assignUserRoles(id,user.getRoleIds());
    }

    /**
     * 保存用户信息
     */
    @Transient
    public void editUser(String userId ,UserVo userView) {
        userView.setUserId(userId);
        //校验
        if (MyStringUtil.isNullOrEmpty(userView.getUsername())){
            throw new GlobalException(ResultCode.NULL,"用户名不能为空！");
        }
//        if (MyStringUtil.isNullOrEmpty(userView.getUserPassword())){
//            throw new GlobalException(ResultCode.NULL,"密码不能为空！");
//        }
        if (!MyStringUtil.isNullOrEmpty(userView.getUserPassword())){
            userView.setUserPassword(Md5Utils.myEncryptPassword(userView.getUserPassword(),userView.getUsername(),10));
        }else {
            userView.setUserPassword(null);
        }
        //解除账户和员工外键关系 由于外键原因只能设为 null
//        if (MyStringUtil.isNullOrEmpty(userView.getEmployeeId())){
//            User userDB = userMapper.selectByPrimaryKey(userView);
//            userDB.setEmployeeId(null);
//            userMapper.updateByPrimaryKey(userDB);
//        }
        UserExample userExample = new UserExample();
        userExample.createCriteria().andUserIdEqualTo(userView.getUserId());
        userMapper.updateByExampleSelective(userView, userExample);
        this.assignUserRoles(userView.getUserId(),userView.getRoleIds());
    }

    /**
     * 根据ID删除一个用户
     */
    @Transient
    public void deleteUserById(String id) {
        userMapper.deleteByPrimaryKey(id);
    }

    /**
     * 批量删除用户
     */
    @Transient
    public int deletes(String ids) {
        return  userMapper.deleteByIds(ids);
    }

    /**
     * 设置允许上传的图片的类型
     */
    private static final List<String> SUFFIXES = Arrays.asList("image/png", "image/jpeg","image/gif");

//    public String uploadImage(String userId,MultipartFile file) {
//
//        try {
//            User user = userMapper.selectByPrimaryKey(userId);
//            //1.验证文件
//            //验证文件的类型
//            String contentType = file.getContentType();
//            if (!SUFFIXES.contains(contentType)) {
//
//                throw new GlobalException(ResultCode.IMAGEFORMATWRONG);
//            }
//            //验证文件的内容
//
//            BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
//            if (bufferedImage == null) {
//                throw new GlobalException(ResultCode.IMAGECONTENTERROR );
//            }
//
//            // 2、将图片上传到FastDFS
//            // 2.1、获取文件后缀名
//            String extension = StringUtils.substringAfterLast(file.getOriginalFilename(), ".");
//            // 2.2、上传
//            StorePath storePath = this.storageClient.uploadImageAndCrtThumbImage(
//                    file.getInputStream(), file.getSize(), extension, null);
////            //完整url
////            String fullImage =  "images.hospital.com:8001/"+storePath.getFullPath();
//
//            // 获取缩略图路径
//            String path = thumbImageConfig.getThumbImagePath(storePath.getFullPath());
//
//            //缩略图url
//            String thumbImage = "http://images.hospital.com:8001/"+path;
//
//            Employee employee = employeeMapper.selectByPrimaryKey(user.getEmployeeId());
//            employee.setImages(thumbImage);
//            int i = employeeMapper.updateByPrimaryKey(employee);
//            if (i>0){
//                return thumbImage;
//            }
//            return null;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//
//    }

    /**
     * 分配用户角色
     * @param userId 用户id
     * @param roleIdsVo 角色set集合id
     */
    private void assignUserRoles(String userId,Set<String> roleIdsVo) {
        if (roleIdsVo.isEmpty()){
            return;
        }
        //存储前端更改的ids列表
        ArrayList<String> idsVo = new ArrayList<>(roleIdsVo);
        //存储数据库user_role已有的roleId
        List<  String > roleIdsDb = new ArrayList<>();

        //根据用户id找到中间表user_role包含用户id的所有信息
        List<UserAndRole> userAndRoles = userMapper.selectUserAndRoleListByUserId(userId);
        userAndRoles.forEach( ur -> roleIdsDb.add(ur.getRoleId()));
        List< UserAndRole > insertUserAndRoles = new ArrayList<>();
        //  删除的角色ids
        List< String > deleteIds = roleIdsDb.stream()
                .filter(id -> !idsVo.contains(id)).collect(Collectors.toList());

        //维护中间表
        idsVo.stream()
                .filter(idVo -> !roleIdsDb.contains(idVo)&&!"".equals(idVo))
                    .forEach( id -> insertUserAndRoles.add(new UserAndRole(idWorker.nextId(),userId,id)) );

        if (insertUserAndRoles.size()>0){
            userMapper.batchInsertUserAndRoleList(insertUserAndRoles);
        }

        if (deleteIds.size()>0){
             userMapper.batchDeleteUserAndRoleListByUserIdAndRoleIds(userId,deleteIds);
        }
    }
}
