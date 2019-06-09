package com.kalvin.kvf.comm.constant;

/**
 * 【作用】表单控件类型 枚举类<br>
 * 【说明】（无）
 * @author Kalvin
 * @Date 2019/4/26 10:00
 */
public enum ControlEnum {

    INPUT("input", "文本框"),
    SELECT("select", "下拉框"),
    RADIO("radio", "单选框"),
    CHECKBOX("checkbox", "多选框"),
    FILE("file", "文件");

    private String type;
    private String name;

    ControlEnum(String type, String name) {
        this.type = type;
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
