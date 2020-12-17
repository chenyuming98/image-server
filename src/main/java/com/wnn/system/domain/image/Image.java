package com.wnn.system.domain.image;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Image {


    @Id
    private String fileId;

    private String fileName;

    private String url;

    private String trueUrl;

    private long fileSize;

}
