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
public class Forecast {


    @Id
    private String id;

    private String fileName;

    private String probability;

    private String classification;

    private String url;

    private String label;

    private String svmInfo;

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
