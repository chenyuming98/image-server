package com.wnn.system.vo;

import com.wnn.system.domain.system.Dictionary;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
* Created by Mybatis Generator on 2020/04/05
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DictionaryVo extends Dictionary {

   /**
    * 控制列表一级菜单操作栏 有添加按钮，二级菜单无添加按钮
    */
   private Boolean isOneMenuInfo;


   /**
    * 重写systemCode 转化为布尔值isEdit，
    * isEdit（重写systemCode=1） 真不可编辑 ， isEdit（重写systemCode=0）可编辑
    */
   private Boolean isEdit;

   private List<DictionaryVo> children;

   @Override
   public void setSystemCode(Integer systemCode) {
      if (systemCode==1){
         isEdit = false;
      }else {
         isEdit = true;
      }
      super.setSystemCode(systemCode);
   }
}