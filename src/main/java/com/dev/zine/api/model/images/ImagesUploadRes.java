package com.dev.zine.api.model.images;

import lombok.Data;

@Data
public class ImagesUploadRes {
    private String url;
    private String publicId;
    private String message;
    private String status;
}
