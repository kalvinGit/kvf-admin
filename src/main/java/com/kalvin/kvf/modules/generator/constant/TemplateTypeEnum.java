package com.kalvin.kvf.modules.generator.constant;

/**
 * 代码模板类型 枚举
 * Create by Kalvin on 2019/6/19.
 */
public enum TemplateTypeEnum {

    ENTITY("ENTITY", "entity.vm"),
    MAPPER("MAPPER", "mapper.vm"),
    SERVICE("SERVICE", "service.vm"),
    SERVICE_IMPL("SERVICE_IMPL", "serviceImpl.vm"),
    CONTROLLER("CONTROLLER", "controller.vm"),
    MAPPER_XML("XML", "mapperXml.vm"),
    TABLE("TABLE", "table.vm"),
    TREE_GRID("TREE_GRID", "treegrid.vm"),
    OPERATION("OPERATION", "operation.vm");

    private String type;
    private String name;

    TemplateTypeEnum(String type, String name) {
        this.type = type;
        this.name = name;
    }

    TemplateTypeEnum get(String type) {
        for (TemplateTypeEnum typeEnum : TemplateTypeEnum.values()) {
            if (typeEnum.getType().equals(type)) {
                return typeEnum;
            }
        }
        return null;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
