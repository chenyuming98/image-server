package com.wnn.system.common.utils;

public interface UtilConstants {

    /**
     *
     * 路径
     *
     */
    public static class ProjectPath {
        public static  String PATH = ""; //项目路径
        public static  String ROOT_PATH = ""; //根项目路径
        public  final static  String SVM_FILE = ""; //根项目路径
        public  final static  String SVM_DAT_FILE = "./temp/svm/"; //svm文件保存路径
        public static final String VIRTUAL_PATH = "/imageLoad"; //虚拟路径
        public static final String VIRTUAL_PATH_CACHE = "/imageLoad/cache"; //虚拟路径
        public static final String CACHE_PATH = "/temp/cache"; //缓存图片路径
    }




    /**
     * 
     * 公共常量
     *
     */
    public static class Public {
        public static final String ID = "TESTID";
    }
    
    /**
     * JSP路径
     */
    public static class JspFilePath {
        public static final String TESTCONTROLLER = "jsp/basic/"; 
        public static final String TEMPLATE_PAGEPATH = "basic/template/"; // 模板（测试）
    }
    
    /**
     * vo 对象的一些公共的属性名称 
     *
     */
    public static class VoFields {
        public static final String ACTIONTIME = "operateTime";//操作时间
        public static final String ACTIONUSERNAME = "operatorName";//操作人姓名
        public static final String CHECKTIME = "auditTime";//审核时间
        public static final String CHECKUSERID = "checkUserId";//审核人ID
        public static final String CHECKUSERNAME = "auditPerson";//审核人姓名
        public static final String CREATETIME = "createTime";        // 创建时间
        public static final String CREATEUSERID = "createUserId";// 创建人code
        public static final String INSERTUSERNAME = "createUserName";// 创建人姓名
        public static final String UPDATETIME = "updateTime";        // 修改时间
        public static final String UPDATEUSERID = "updateUserId";// 修改人CODE
        public static final String UPDATEUSERNAME = "updateUserName";// 修改人姓名
        public static final String DELFLAG = "delFlag";             // 删除标记
        public static final String DBID = "dbid";                    // 主键
    }
    
    
    
}