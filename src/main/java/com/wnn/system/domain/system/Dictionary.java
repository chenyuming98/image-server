package com.wnn.system.domain.system;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;

/**
* Created by Mybatis Generator on 2020/04/05
*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Dictionary {

    @Id
    private String dictionaryId;


    /**
     * 字典名称
     */
    private String dictionaryName;

    /**
     * 字典代码
     */
    private String dictionaryCode;

    /**
     * 字典值
     */
    private String dictionaryValue;

    /**
     * 字典名称
     */
    private String dictionaryParentId;


    /**
     * 描述
     */
    private String dictionaryInfo;

    /**
     * 系统字段 1不可删除
     */
    private Integer systemCode;
}