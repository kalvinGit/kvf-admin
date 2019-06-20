package com.kalvin.kvf.common.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author Kalvin
 */
@Data
@ToString
public class UploadFileInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;
    private String path;
    private String absolutePath;

    public UploadFileInfo(String name, String path, String absolutePath) {
        this.name = name;
        this.path = path;
        this.absolutePath = absolutePath;
    }
}
