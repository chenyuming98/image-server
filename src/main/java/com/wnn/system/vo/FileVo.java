package com.wnn.system.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileVo {

    @Id
    private String fileId;

    private String title;

    private String url;

    private String icon = "el-icon-folder-opened";

    private List<FileVo> children;

}
