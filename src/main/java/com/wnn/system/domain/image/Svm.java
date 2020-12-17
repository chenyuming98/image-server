package com.wnn.system.domain.image;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Id;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Svm {

    @Id
    private String svmId;

    private String name;

    private String info;


    /**
     * SVM类型
     */
    private Integer svmType;

    /**
     * SVM内核
     */
    private Integer svmKernel;

    /**
     * 惩罚因子
     */
    private String svmC;

    /**
     * 图片归一化X
     */
    private Integer winSizeX;

    /**
     * 图片归一化Y
     */
    private Integer winSizeY;

    /**
     * 每个块大小X
     */
    private Integer blockSizeX;

    /**
     * 每个块大小Y
     */
    private Integer blockSizeY;

    /**
     * 快滑动增量X
     */
    private Integer blockStrideSizeX;

    /**
     * 快滑动增量Y
     */
    private Integer blockStrideSizeY;

    /**
     * 细胞元大小X
     */
    private Integer cellSizeX;

    /**
     * 细胞元大小Y
     */
    private Integer cellSizeY;

    /**
     * 特征数量
     */
    private Integer countHogNum;

    /**
     * 纬数
     */
    private Integer dimension;

    /**
     * 创建时间
     */
    private Date createTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    public Date getCreateTime() {
        return createTime;
    }
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
