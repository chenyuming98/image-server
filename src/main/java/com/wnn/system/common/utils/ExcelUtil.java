package com.wnn.system.common.utils;

import com.wnn.system.domain.base.ResultCode;
import com.wnn.system.frame.advice.GlobalException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Excle导入解析工具
 */
public class ExcelUtil {


    public  static List<List<String>> readExcle(File file){

        if (file.getName().toLowerCase().endsWith(".xlsx")){
            return readXlsxExcle(file);
        }else if (file.getName().toLowerCase().endsWith(".tmp")){
            return readXlsxExcle(file);
        }else {
            throw new GlobalException(ResultCode.FALSE,"文件名称不匹配");
        }
    }


    public static List<List<String>> readXlsxExcle(File file){
        List<List<String>>  excleRowList =  new ArrayList<>();
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(fileInputStream);
            for (int k = 0; k < xssfWorkbook.getNumberOfSheets(); k++) {//获取每个Sheet表
                //获取第一页表格
                XSSFSheet sheet = xssfWorkbook.getSheetAt(k);
                for (int j = 0; j <= sheet.getLastRowNum(); j++) {
                    //获取一行解析
                    XSSFRow row = sheet.getRow(j);
                    ArrayList<String> rowObject = new ArrayList<>();
                    for (int i = 0; i <= row.getLastCellNum(); i++) {
                        //获取一行 一列 填充
                        XSSFCell cell = row.getCell(i);
                        rowObject.add(parsingCellValue(cell));

                    }
                    excleRowList.add(rowObject);
                }
            }
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw  new GlobalException(ResultCode.FALSE,"文件异常！");
        }
        return excleRowList;
    }


    /**
     * 根据列 数据类型 解析成字符串返回
     * @param cell
     * @return
     */
    public static String parsingCellValue(Cell cell){
        if (cell==null){
            return "";
        }else {
            switch (cell.getCellType()){
                case STRING:
                    return cell.getStringCellValue();
                case BOOLEAN:
                    return String.valueOf(cell.getStringCellValue());
                case ERROR:
                    return String.valueOf(cell.getStringCellValue());
                case NUMERIC:
                    return String.valueOf(cell.getNumericCellValue());
            }
        }
        return "";
    }
}
