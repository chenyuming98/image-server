package com.wnn.system.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wnn.system.common.utils.*;
import com.wnn.system.config.TasksTimer;
import com.wnn.system.domain.base.BaseResult;
import com.wnn.system.domain.base.ResultCode;
import com.wnn.system.domain.image.Forecast;
import com.wnn.system.domain.image.Image;
import com.wnn.system.domain.image.Svm;
import com.wnn.system.frame.advice.GlobalException;
import com.wnn.system.mapper.ForecastMapper;
import com.wnn.system.mapper.SvmMapper;
import com.wnn.system.mapper.UserMapper;
import com.wnn.system.vo.FileVo;
import com.wnn.system.vo.SvmVo;
import org.opencv.core.Mat;
import org.opencv.ml.Ml;
import org.opencv.ml.SVM;
import org.opencv.objdetect.HOGDescriptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *  文件管理模块
 */
@Service
public class FileService {

    @Autowired
    private SvmMapper svmMapper;

    @Autowired
    private ForecastMapper forecastMapper;

    @Autowired
    private IdWorker idWorker;

    /**
     * 根据分拣目录获取图片信息
     */
    public PageInfo<Image> findList(String loadPath, Integer page, Integer size) {

        String  path = System.getProperty("user.dir") + "\\file\\"+loadPath + "\\";;
        File dirFile = new File(path);
        File[] files = dirFile.listFiles();
        if (files ==null){
            return null;
        }
        List<Image> imageList = new ArrayList<>(files.length);
        int begin =(page-1)*size;
        int end = (page-1)*size +size ;
        for (int i =  begin; i < files.length && i<end; i++) {
            if (files[i].isFile()) {
                Image image = new Image();
                image.setFileName(files[i].getName());
                image.setFileSize(files[i].length());
                image.setUrl( UtilConstants.ProjectPath.VIRTUAL_PATH+"/"+ loadPath +"/" +files[i].getName());
                image.setTrueUrl(loadPath +"/" +files[i].getName());
                imageList.add(image);
            }
        }
        PageInfo pageInfo = new PageInfo<>();
        int total = files.length;
        pageInfo.setList(imageList);
        pageInfo.setTotal(total);
        return pageInfo;
    }

    /**
     * 获取文件目录
     */
    public  List<FileVo> findFileList() {
        File dirFile = new File( UtilConstants.ProjectPath.PATH);
        File[] files = dirFile.listFiles();
        ArrayList<FileVo> fileVos = new ArrayList<>();
        if (files !=null){
            for (File f : files) {
                String fileName = f.getName();
                FileVo fileVo = new FileVo();
                fileVo.setFileId(UUID.randomUUID()+"");
                fileVo.setTitle(fileName);
                fileVo.setUrl(fileName);
                String cPath=f.getAbsolutePath();
                File cDirFile = new File( cPath);
                File[] cFiles = cDirFile.listFiles();
                ArrayList<FileVo> cFileVos = new ArrayList<>();
                if (cFiles!=null){
                    for (File cf : cFiles) {
                        String cfName = cf.getName();
                        String cUrl = fileName+ "\\"+cfName;
                        FileVo cFileVo = new FileVo();
                        cFileVo.setFileId(UUID.randomUUID()+"");
                        cFileVo.setTitle(cfName);
                        cFileVo.setUrl(cUrl);
                        cFileVos.add(cFileVo);
                    }
                }
                fileVo.setChildren(cFileVos);
                fileVos.add(fileVo);
            }
        }
        return fileVos;
    }

    /**
     * 批量删除图片
     */
    public  int batchDelete(List<String> urls) {
        if (urls.isEmpty()){
            return 0;
        }else {
            String path = UtilConstants.ProjectPath.PATH + "\\";
            for (String s:urls) {
                String truePath = path + s;
                File file = new File(truePath);
                deleteOsFile(file);
            }

            return 1;
        }
    }

    /**
     * 递归删除文件
     */
    private static void deleteOsFile(File file){
        //判断文件不为null或文件目录存在
        if (file == null || !file.exists()){
            System.out.println("文件删除失败,请检查文件路径是否正确");
            return;
        }
        //取得这个目录下的所有子文件对象
        File[] files = file.listFiles();
        //遍历该目录下的文件对象
        for (File f: files){
            //打印文件名
            String name = file.getName();
            System.out.println(name);
            //判断子目录是否存在子目录,如果是文件则删除
            if (f.isDirectory()){
                deleteOsFile(f);
            }else {
                f.delete();
            }
        }
        //删除空文件夹  for循环已经把上一层节点的目录清空。
        file.delete();
    }

    /**
     * SVM训练
     */
    public void trainSvm(SvmVo svmVo) {
        ImageUtil imageUtil = new ImageUtil();
        HashMap<String, List<String>> trainPath = imageUtil.getImageFilePathTow(svmVo.getUrl());
        String time = System.currentTimeMillis()+".dat";
        Svm svmPojo = svmVo;
        svmPojo.setSvmId(idWorker.nextId());
        svmPojo.setName(time);
        svmPojo.setCreateTime(new Date());
        HashMap<String, Mat> hogMat = imageUtil.getHogFeature(trainPath,svmPojo);
        SVM svmModel = SVM.create();
        svmModel.setType(svmPojo.getSvmType());
        svmModel.setKernel(svmPojo.getSvmKernel());
        svmModel.setC(new Double(svmPojo.getSvmC()));
        svmModel.train(hogMat.get("data"), Ml.ROW_SAMPLE, hogMat.get("resp"));
        svmModel.save(UtilConstants.ProjectPath.SVM_DAT_FILE+svmPojo.getName());
        svmMapper.insert(svmPojo);
    }

    /**
     * 获取SVM文件列表
     */
    public List<Svm> svmList() {
        Example example = new Example(Svm.class);
        example.orderBy("createTime").desc();
        return svmMapper.selectByExample(example);

    }

    public PageInfo<Svm> svmInfoList(Integer page, Integer size) {
        Example example = new Example(Forecast.class);
        example.orderBy("createTime").desc();
        PageHelper.startPage(page,size);
        List<Svm> svmList = svmMapper.selectByExample(example);
        return new PageInfo<>(svmList);
    }

    /**
     * 测试集测试、训练结果
     */
    public void svmTest(Map<String, Object> submitInfo) {
        ImageUtil imageUtil = new ImageUtil();
        String fileUrl = (String) submitInfo.get("fileUrl");
        LinkedHashMap<String,Object> svmVo = (LinkedHashMap<String,Object>) submitInfo.get("svmPojo");
        Svm svmPojO = svmMapper.selectByPrimaryKey(svmVo.get("svmId"));
        String name = svmPojO.getName();
        SVM svmModel = SVM.load(UtilConstants.ProjectPath.SVM_DAT_FILE+name);
        HashMap<String, List<String>> matchPath = imageUtil.getImageFilePathTow(fileUrl);
        imageUtil.dealStringToIntLabMap(svmPojO);//标签处理
        ArrayList<Forecast> forecasts = imageUtil.SvmTestPredict(matchPath, svmModel,svmPojO);
        String virtualPath = UtilConstants.ProjectPath.VIRTUAL_PATH;
        //处理URL成虚拟路径，添加入库
        for (Forecast f:forecasts
        ) {
            String url = f.getUrl();
            if (!MyStringUtil.isNullOrEmpty(url)){
                String[] urls = url.split("file");
                f.setId(idWorker.nextId());
                f.setFileName(urls[1]);
                f.setUrl(virtualPath+urls[1]);
                f.setSvmInfo(svmPojO.getInfo());
            }
            forecastMapper.insert(f);
        }

    }

    /**
     * 测试集测试、训练结果
     */
    public PageInfo<Forecast> probabilityList(Integer page, Integer size) {
        Example example = new Example(Forecast.class);
        example.orderBy("createTime").desc();
        example.orderBy("classification").desc();
        PageHelper.startPage(page,size);
        List<Forecast> forecasts = forecastMapper.selectByExample(example);
        return new PageInfo<>(forecasts);
    }

    /**
     * 批量删除预测信息
     * @param ids
     * @return
     */
    public int batchDeleteProbability(String ids) {
        return forecastMapper.deleteByIds(ids);
    }

    /**
     * 检测图片
     */
    public void uploadOneImage(File fileObject, String originalFilename,String svmId) {
        String path = UtilConstants.ProjectPath.CACHE_PATH  + "\\" +originalFilename;
        Svm svmPojO = svmMapper.selectByPrimaryKey(svmId);
        SVM svmModel = SVM.load(UtilConstants.ProjectPath.SVM_DAT_FILE + svmPojO.getName());
        //获取到的标签结构要进行处理  tterier:0;panda:1;cat:2;jingyu:3;camera:4;dog:5 编制成map映射
        String info = svmPojO.getInfo(); //得到tterier:0;panda:1;cat:2;jingyu:3;camera:4;dog:5
        String[] c = info.split(";"); //tterier:0
        HashMap<String, Integer> labMap = new HashMap<>();
        for (String primeKey :c
        ) {
            String[] keyAndValue = primeKey.split(":"); //tterier:0
            labMap.put(keyAndValue[0],Integer.valueOf(keyAndValue[1]));
        }
        ImageUtil imageUtil = new ImageUtil();
//        imageUtil.SvmPredict();
    }

    /**
     * 批量删除SVM文件
     */
    public int deletesSvmList(String ids) {
        return svmMapper.deleteByIds(ids);
    }


    /**
     * 上传一张、多张图片检测
     */
    public void uploadImages(MultipartFile[] multipartFiles, String svmId) {
        Svm svmPojO = svmMapper.selectByPrimaryKey(svmId);
        if (svmPojO==null){
            throw new GlobalException(ResultCode.ERROR,"SVM文件不存在或者异常！");
        }
        String cachePath = UtilConstants.ProjectPath.ROOT_PATH
                + UtilConstants.ProjectPath.CACHE_PATH + "\\";
        //获取当前时间戳
        long timeInMillis = Calendar.getInstance().getTimeInMillis();
        String timeStr = String.valueOf(timeInMillis);
        ArrayList<String> filePath = new ArrayList<>();
        for(int i = 0;i<multipartFiles.length;i++) {
            MultipartFile file = multipartFiles[i];
            try {
                //图片保存到本地缓存
                String path = cachePath + timeStr + file.getOriginalFilename();
                file.transferTo(new File(path));
                filePath.add(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ImageUtil imageUtil = new ImageUtil();
        imageUtil.dealStringToIntLabMap(svmPojO);
        //加载svm - model
        String name = svmPojO.getName();
        SVM svmModel = SVM.load(UtilConstants.ProjectPath.SVM_DAT_FILE+name);
        ArrayList<Forecast> forecasts = imageUtil.svmPredict(filePath, svmModel, svmPojO);
        String virtualPath = UtilConstants.ProjectPath.VIRTUAL_PATH;
        //处理URL成虚拟路径，添加入库
        for (Forecast f:forecasts
        ) {
            String url = f.getUrl();
            System.out.println(url);
            if (!MyStringUtil.isNullOrEmpty(url)){
                String[] urls = url.split("temp");
                f.setId(idWorker.nextId());
                f.setFileName(urls[1]);
                f.setUrl(virtualPath+urls[1]);
                f.setSvmInfo(svmPojO.getInfo());
            }
            forecastMapper.insert(f);
        }
    }



}
