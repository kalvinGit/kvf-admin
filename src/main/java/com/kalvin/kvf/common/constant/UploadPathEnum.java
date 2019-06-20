package com.kalvin.kvf.common.constant;

/**
 * 上传路径枚举
 * @author Kalvin
 */
public enum UploadPathEnum {
    FILE_PATH(0, "static/upload/file/"),
    IMAGE_PATH(1, "static/upload/image/"),
    DOC_PATH(2, "static/upload/doc/"),
    HEAD_PATH(3, "static/image/avatar/");  // 用户头像

    private int type;
    private String path;

    UploadPathEnum(int type, String path) {
        this.type = type;
        this.path = path;
    }

    public static UploadPathEnum get(int type) {
        for (UploadPathEnum pathEnum : values()) {
            if (pathEnum.getType() == type) {
                return pathEnum;
            }
        }
        return null;
    }

    public int getType() {
        return type;
    }

    public String getPath() {
        return path;
    }
}
