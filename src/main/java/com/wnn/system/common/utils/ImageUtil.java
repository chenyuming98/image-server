package com.wnn.system.common.utils;

import com.wnn.system.domain.base.ResultCode;
import com.wnn.system.domain.image.Forecast;
import com.wnn.system.domain.image.Image;
import com.wnn.system.domain.image.Svm;
import com.wnn.system.frame.advice.GlobalException;
import com.wnn.system.vo.FileVo;
import lombok.SneakyThrows;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.ml.Ml;
import org.opencv.ml.SVM;
import org.opencv.objdetect.HOGDescriptor;

import java.io.File;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;


/**
 *  图像处理类
 */
public class ImageUtil {

    /**
     * 目录标签  train/cat ： 1
     */
    private HashMap<String,Integer> labMap;

    /**
     * 设置标签类MAP
     */
    public HashMap<String,Integer> setLabMap(HashMap<String,Integer> map) {
        return this.labMap = map;
    }


    public ImageUtil(){
    }

    /**
     * 获取图片路径
     * @param loadPath 默认是 项目路径下的 file下目录
     * @return
     */
    public HashMap<String, List<String>> getImageFilePath(String loadPath){
        HashMap<String, List<String>> filePath = new HashMap<>();
        String  path = System.getProperty("user.dir") + "\\file\\"+loadPath + "\\";;
        File dirFile = new File(path);
        if (dirFile.exists()) {
            File[] files = dirFile.listFiles();
            if (files != null) {
                ArrayList<String> clist = new ArrayList<>();
                for (File fileChildDir : files) {
                    //输出文件名或者文件夹名
//                    System.out.println(fileChildDir.getName());
                    if (fileChildDir.isDirectory()) {
//                        System.out.println(" :  此为目录名");
                        //通过递归的方式,可以把目录中的所有文件全部遍历出来
//                        System.out.println(fileChildDir.getAbsolutePath() );
                        String cPath=fileChildDir.getAbsolutePath();
                        File file=new File(cPath);
                        File[] tempList = file.listFiles();
                        if(tempList.length==0){
                            continue;
                        }
                        ArrayList<String> everyFilePath = new ArrayList<>();
                        for (int i = 0; i < tempList.length; i++) {
                            if (tempList[i].isFile()) {
//                                System.out.println("文  件："+tempList[i]);
                                everyFilePath.add(tempList[i].toString());
                            }
                        }
                        filePath.put(fileChildDir.getName(), everyFilePath);
                    }else {
                        String cPath=fileChildDir.getAbsolutePath();
                        clist.add(cPath);
                    }
                }
//                filePath.put("child",clist);
            }
        }else{
            System.out.println("你想查找的文件不存在");

        }
        return filePath;
    }


    /**
     * 对于每个目录进行处理获取路径
     * @param loadPath
     * @return
     */
    public HashMap<String, List<String>> getImageFilePathTow(String loadPath){
        if (MyStringUtil.isNullOrEmpty(loadPath)){
            throw new GlobalException(ResultCode.NULL,"Url为空");
        }
        String[] splitUrl = loadPath.split(",");
        if (splitUrl.length==0){
            throw new GlobalException(ResultCode.NULL,"文件列表解析为空");
        }
        HashMap<String, List<String>> filePath = new HashMap<>();
        String  path = System.getProperty("user.dir") + "\\file\\";;
        for (String everyUrl:splitUrl
             ) {
            String nowPath = path+"\\"+everyUrl;
            //1.获取目录
            File dirFile = new File(nowPath);
            if (dirFile.exists()) {
                File[] files = dirFile.listFiles();
                //目录下不为空获取图像列表组装
                if (files != null) {
                    ArrayList<String> cList = new ArrayList<>();
                    for (File fileChildDir : files) {
                        String cPath=fileChildDir.getAbsolutePath();
                        cList.add(cPath);
                    }
                    filePath.put(everyUrl,cList);
                }
            }else{
                System.out.println("你想查找的文件不存在");
            }
        }
        return filePath;
    }


    /**
     * 获取 文件HOG特征
     * @return
     */
    public HashMap<String, Mat> getHogFeature(HashMap<String,  List<String>> file, Svm svm){
        ArrayList<float[]> descriptors = new ArrayList<float[]>();
        /*
         *由于局部光照的变化以及前景-背景对比度的变化，使得梯度强度的变化范围非常大。这就需要对梯度强度做归一化。归一化能够进一步地对光照、
         * 阴影和边缘进行压缩。我们把各个细胞单元组合成大的、空间上连通的区间（blocks）。这样，一个block内所有cell的特征向量串联起来便得到该block的HOG特征。这些区间是互有重叠的，
         * 这就意味着：每一个单元格的特征会以不同的结果多次出现在最后的特征向量中。我们将归一化之后的块描述符（向量）就称之为HOG描述符。
         *例如在行人检测中，图像大小为64*128，我们的细胞单元大小为8*8像素大小，那么我们将每2*2个cell组成一个block，也就是一个block内有16*16像素，因此之前一个cell里面有9个特征，
         * 因此一个block将4个cell的特征串联，就有4*9=36个特征，然后，我们将这36个特征进行最大最小值归一化。
         *我们以8个像素为步长滑动这个block，水平方向我们可以扫描出7个block，竖直方向可以扫描出15个block，因此64*128的图像最后的特征有36*7*15=3780个特征。下图显示了定向梯度直方图的可视化：
         */
        int SAMPLE_COUNT = 0; //样本数
        int PICTURE_FEATURE_DIM = svm.getDimension();//图片特征维数,64*64(1764),64*128(36*7*15=3780),128*128(8100)

        ArrayList<Float> img_label = new ArrayList<Float>();

        Size winSize = new Size(svm.getWinSizeX(),svm.getWinSizeY()); // 窗口大小
        Size blockSize = new Size(svm.getBlockSizeX(),svm.getBlockSizeY()); //块大小
        Size blockStrideSize = new Size(svm.getBlockStrideSizeX(),svm.getBlockStrideSizeY()); //块滑动增量
        Size cellSize = new Size(svm.getCellSizeX(),svm.getCellSizeY()); //胞元大小
        Integer countHogNum = svm.getCountHogNum();//直方图特征数量
//        aqual to HOGDescriptor(Size(64,128), Size(16,16), Size(8,8), Size(8,8), 9 )
        HOGDescriptor hogDescriptor=new HOGDescriptor(winSize,blockSize,blockStrideSize,cellSize,countHogNum);
        ArrayList<Integer> labList = new ArrayList<>();
        int label = 0;
        StringBuilder stringBuilder = new StringBuilder();
        for (HashMap.Entry<String,  List<String>> entry:file.entrySet()
        ) {
            String key = entry.getKey();
            System.out.println("当前处理hog特征类："+ key+  "   标签为"+ label);
            stringBuilder.append(key+":" + label +";");
            List<String> imgPathList = entry.getValue();
            for (String s : imgPathList) {
                Mat src = Imgcodecs.imread(s,Imgcodecs.IMREAD_COLOR);
                if (src.empty()){
                    System.out.println("null:"+s);
                    continue;
                }
                Mat img = new Mat();
                Imgproc.resize(src, img,winSize);
                MatOfFloat descriptorsOfMat = new MatOfFloat();
                hogDescriptor.compute(img,descriptorsOfMat,blockStrideSize,new Size(0,0));
                float[] descriptor = descriptorsOfMat.toArray();//一列
                descriptors.add(descriptor);
                labList.add(label);
                img_label.add((float) label);
                SAMPLE_COUNT++;
            }
            label = label+1;
        }
        Mat data_mat = new Mat(SAMPLE_COUNT, PICTURE_FEATURE_DIM, CvType.CV_32FC1);//行，列，类型
        Mat res_mat = new Mat(SAMPLE_COUNT, 1, CvType.CV_32SC1);
        HashMap<String, Mat> map = new HashMap<>(2);
        for (int m = 0; m < descriptors.size(); m++) {
            for (int n = 0; n < descriptors.get(m).length; n++) {
                float v = descriptors.get(m)[n];
                data_mat.put(m, n, v);//按行存储
            }
            res_mat.put(m, 0, img_label.get(m));//一列向量
        }
        map.put("data",data_mat);
        map.put("resp",res_mat);
        if (svm!=null){
            svm.setInfo(stringBuilder.toString());
        }
        return map;
    }

    /**
     * SVM 结果预测
     * @param file
     * @param hogDescriptor
     * @param svm
     */
    public void SvmPredict(  HashMap<String,  List<String>> file,HOGDescriptor hogDescriptor , SVM svm ){
        ArrayList<String> resultList = new ArrayList<>();
        for ( Map.Entry<String, List<String>> m :file.entrySet()) {
            String key = m.getKey();
            List<String> child = m.getValue();
            if (child.isEmpty()){
                continue;
            }
            resultList.add("******************预测结果："+key+"***************************");
            Integer classLab = labMap.get(key);//文件夹种类标签
            float matchFloat = new Float(classLab);
            BigDecimal rightNum = new BigDecimal("0.00");  //标签计算正确数
            BigDecimal countNum = new BigDecimal("0.00"); //文件夹下总数
            BigDecimal one = new BigDecimal("1.00"); //计数
            for (String s : child) {
                Mat src = Imgcodecs.imread(s,Imgcodecs.IMREAD_COLOR);
                Mat img = new Mat();
                Imgproc.resize(src, img,new Size(64,128));
                //  Imgproc.cvtColor(img,img,Imgproc.COLOR_BGR2GRAY);
                MatOfFloat descriptorsOfMat = new MatOfFloat();
                hogDescriptor.compute(img,descriptorsOfMat,new Size(8,8),new Size(0,0));
                Mat reshape = descriptorsOfMat.reshape(0, 1);
                float predict = svm.predict(reshape);
                if(matchFloat == predict){
                    rightNum = rightNum.add(one);//正确数量
                }
                countNum = countNum.add(one);//文件夹下总数
                String result = s + "*********************" + predict;
                resultList.add(result);
            }
            BigDecimal resultNum = rightNum.divide(countNum, 2, BigDecimal.ROUND_HALF_UP);
            resultList.add("*******************正确率："+resultNum+"**************************");
        }
        for (String s:resultList
        ) {
            System.out.println(s);
        }
    }

    /**
     * SVM 结果测试集结果检测
     *  按目录匹配有正确结果可以统计
     * @param file
     * @param svm
     * @param svmPojO
     */
    public ArrayList<Forecast>  SvmTestPredict(HashMap<String, List<String>> file, SVM svm, Svm svmPojO){
        Size winSize = new Size(svmPojO.getWinSizeX(),svmPojO.getWinSizeY()); // 窗口大小
        Size blockSize = new Size(svmPojO.getBlockSizeX(),svmPojO.getBlockSizeY()); //块大小
        Size blockStrideSize = new Size(svmPojO.getBlockStrideSizeX(),svmPojO.getBlockStrideSizeY()); //块滑动增量
        Size cellSize = new Size(svmPojO.getCellSizeX(),svmPojO.getCellSizeY()); //胞元大小
        Integer countHogNum = svmPojO.getCountHogNum();//直方图特征数量

//        aqual to HOGDescriptor(Size(64,128), Size(16,16), Size(8,8), Size(8,8), 9 )
        HOGDescriptor hogDescriptor=new HOGDescriptor(winSize,blockSize,blockStrideSize,cellSize,countHogNum);

        ArrayList<String> resultList = new ArrayList<>();
        Date date = new Date();
        ArrayList<Forecast> forecasts = new ArrayList<>();
        HashMap<Integer, String> intToStringLabMap = new HashMap<>();
        if (labMap!=null&&!labMap.isEmpty()){
            Set<Map.Entry<String, Integer>> entries = labMap.entrySet();
            Iterator<Map.Entry<String, Integer>> iterator = entries.iterator();
            while (iterator.hasNext()){
                Map.Entry<String, Integer> next = iterator.next();
                String lab = next.getKey();
                Integer value = next.getValue();
                intToStringLabMap.put(value,lab);
            }
        }
        for ( Map.Entry<String, List<String>> m :file.entrySet()) {
            String key = m.getKey();
            List<String> child = m.getValue();
            if (child.isEmpty()){
                continue;
            }
            resultList.add("******************预测结果："+key+"***************************");
            String replaceLab = key.replace("match", "train");
            Integer classLab = labMap.get(replaceLab);//文件夹种类标签

            if (classLab!=null){
                Float matchFloat = new Float(classLab);//已知训练集文件夹标签
                BigDecimal rightNum = new BigDecimal("0.00");  //标签计算正确数
                BigDecimal countNum = new BigDecimal("0.00"); //文件夹下总数
                BigDecimal one = new BigDecimal("1.00"); //计数

                for (String s : child) {
                    Mat src = Imgcodecs.imread(s,Imgcodecs.IMREAD_COLOR);
                    Mat img = new Mat();
                    Imgproc.resize(src, img,winSize);
                    //  Imgproc.cvtColor(img,img,Imgproc.COLOR_BGR2GRAY);
                    MatOfFloat descriptorsOfMat = new MatOfFloat();
                    hogDescriptor.compute(img,descriptorsOfMat,blockStrideSize,new Size(0,0));
                    Mat reshape = descriptorsOfMat.reshape(0, 1);
                    float predict = svm.predict(reshape);
                    int predict1 = (int) predict;
                    String matchLab = intToStringLabMap.get(predict1);
                    //构造预测结果
                    Forecast forecast = new Forecast();
                    forecast.setCreateTime(date);
                    forecast.setLabel(String.valueOf(matchFloat));//原始标签
                    forecast.setScore(String.valueOf(predict1));//预测得分
                    forecast.setClassification(matchLab); //根据预测得分得到的分类
                    forecast.setUrl(s);
                    forecasts.add(forecast);
                    if(matchFloat == predict){
                        rightNum = rightNum.add(one);//正确数量
                    }
                    countNum = countNum.add(one);//文件夹下总数
                    String result = s + "*********************" + predict;
                    resultList.add(result);
                }
                BigDecimal resultNum = rightNum.divide(countNum, 2, BigDecimal.ROUND_HALF_UP);
                resultList.add("*******************正确率："+resultNum+"**************************");
                for (Forecast f:forecasts
                ) {
                    f.setProbability(String.valueOf(resultNum));
                }
            }else {
                for (String s : child) {
                    Mat src = Imgcodecs.imread(s,Imgcodecs.IMREAD_COLOR);
                    Mat img = new Mat();
                    Imgproc.resize(src, img,winSize);
                    //  Imgproc.cvtColor(img,img,Imgproc.COLOR_BGR2GRAY);
                    MatOfFloat descriptorsOfMat = new MatOfFloat();
                    hogDescriptor.compute(img,descriptorsOfMat,blockStrideSize,new Size(0,0));
                    Mat reshape = descriptorsOfMat.reshape(0, 1);
                    float predict = svm.predict(reshape);
                    int predict1 = (int) predict;
                    String matchLab = intToStringLabMap.get(predict1);
                    //构造预测结果
                    Forecast forecast = new Forecast();
                    forecast.setCreateTime(date);
                    forecast.setLabel(String.valueOf(predict));
                    forecast.setClassification(matchLab);
                    forecast.setUrl(s);
                    forecasts.add(forecast);
                    String result = s + "*********************" + predict;
                    resultList.add(result);
                }
            }

        }
        for (String s:resultList
        ) {
            System.out.println(s);
        }
        return forecasts;
    }


    /**
     *  SVM 预测图片
     */
    public ArrayList<Forecast>  svmPredict(List<String> filePath, SVM svm, Svm svmPojO){
        Size winSize = new Size(svmPojO.getWinSizeX(),svmPojO.getWinSizeY()); // 窗口大小
        Size blockSize = new Size(svmPojO.getBlockSizeX(),svmPojO.getBlockSizeY()); //块大小
        Size blockStrideSize = new Size(svmPojO.getBlockStrideSizeX(),svmPojO.getBlockStrideSizeY()); //块滑动增量
        Size cellSize = new Size(svmPojO.getCellSizeX(),svmPojO.getCellSizeY()); //胞元大小
        Integer countHogNum = svmPojO.getCountHogNum();//直方图特征数量

        HOGDescriptor hogDescriptor=new HOGDescriptor(winSize,blockSize,blockStrideSize,cellSize,countHogNum);

        ArrayList<String> resultList = new ArrayList<>();
        Date date = new Date();
        ArrayList<Forecast> forecasts = new ArrayList<>();
        HashMap<Integer, String> integerStringHashMap = this.dealIntToStringLabMap();
        for (String path : filePath) {
            Mat src = Imgcodecs.imread(path,Imgcodecs.IMREAD_COLOR);
            Mat img = new Mat();
            Imgproc.resize(src, img,winSize);
            //  Imgproc.cvtColor(img,img,Imgproc.COLOR_BGR2GRAY);
            MatOfFloat descriptorsOfMat = new MatOfFloat();
            hogDescriptor.compute(img,descriptorsOfMat,blockStrideSize,new Size(0,0));
            Mat reshape = descriptorsOfMat.reshape(0, 1);
            float predictFloat = svm.predict(reshape);
            int predictInt = (int) predictFloat;
            String predictStr = integerStringHashMap.get(predictInt);
            //构造预测结果
            Forecast forecast = new Forecast();
            forecast.setCreateTime(date);
//            forecast.setLabel(String.valueOf(predictFloat));
            forecast.setScore(String.valueOf(predictInt));//预测得分
            forecast.setClassification(predictStr);
            forecast.setUrl(path);
            forecasts.add(forecast);
            String result = path + "*********************" + path;
            resultList.add(result);
        }
        for (String s:resultList
        ) {
            System.out.println(s);
        }
        return forecasts;
    }

    /**
     *  映射标签 StringToInt
     */
    public void dealStringToIntLabMap(Svm svmPojO){
        //标签处理
        //获取到的标签结构要进行处理  tterier:0;panda:1;cat:2;jingyu:3;camera:4;dog:5 编制成map映射
        String info = svmPojO.getInfo(); //得到tterier:0;panda:1;cat:2;jingyu:3;camera:4;dog:5
        String[] c = info.split(";"); //tterier:0
        HashMap<String, Integer> labMap = new HashMap<>();
        for (String primeKey :c
        ) {
            String[] keyAndValue = primeKey.split(":"); //tterier:0
            labMap.put(keyAndValue[0],Integer.valueOf(keyAndValue[1]));
        }
        this.setLabMap(labMap);
    }

    /**
     *  映射标签  IntToStringLabMap
     */
    public   HashMap<Integer, String> dealIntToStringLabMap() {
        HashMap<Integer, String> dealIntToStringLabMap = new HashMap<>();
        if (labMap != null && !labMap.isEmpty()) {
            Set<Map.Entry<String, Integer>> entries = labMap.entrySet();
            Iterator<Map.Entry<String, Integer>> iterator = entries.iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Integer> next = iterator.next();
                String lab = next.getKey();
                Integer value = next.getValue();
                dealIntToStringLabMap.put(value, lab);
            }
        }
        return dealIntToStringLabMap;
    }



    @SneakyThrows
    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

    }


}
