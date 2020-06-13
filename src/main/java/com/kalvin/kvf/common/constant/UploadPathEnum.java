package com.kalvin.kvf.common.constant;

/**
 * 上传路径枚举
 * @author Kalvin
 */
public enum UploadPathEnum {
    FILE_PATH("file", "file"),   // 默认其它文件路径
    IMAGE_PATH("image", "image"), // 默认图片路径
    DOC_PATH("doc", "doc"),     // 默认文档路径
    HEAD_PATH("avatar", "avatar"),  // 默认用户头像路径
    UE_EDITOR_PATH("ueditor", "ueditor");  // 默认UEditor上传目录

    private String type;
    private String path;

    UploadPathEnum(String type, String path) {
        this.type = type;
        this.path = path;
    }

    public static UploadPathEnum get(String type) {
        for (UploadPathEnum pathEnum : values()) {
            if (pathEnum.getType().equals(type)) {
                return pathEnum;
            }
        }
        return null;
    }

    public String getType() {
        return type;
    }

    public String getPath() {
        return path;
    }
}
