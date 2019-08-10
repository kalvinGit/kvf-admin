package com.kalvin.kvf.modules.generator.constant;

/**
 * mysql数据库字段类型枚举
 * Create by Kalvin on 2019/8/10
 */
public enum DbColumnTypeEnum {

    VARCHAR("varchar", "String"),
    CHAR("char", "String"),
    TEXT("text", "String"),
    INT("int", "Integer"),
    INTEGER("integer", "Integer"),
    TINYINT("tinyint", "Integer"),
    SMALLINT("smallint", "Integer"),
    MEDIUMINT("mediumint", "Integer"),
    BIT("bit", "Boolean"),
    BIGINT("bigint", "Long"),
    FLOAT("float", "Float"),
    DOUBLE("double", "Double"),
    DECIMAL("decimal", "BigDecimal"),
    DATE("date", "LocalDate"),
    TIME("time", "LocalTime"),
    DATETIME("datetime", "LocalDateTime"),
    TIMESTAMP("timestamp", "LocalDateTime"),
    YEAR("year", "LocalDate"),
    BLOB("blob", "byte[]");

    private String type;
    private String javaType;

    DbColumnTypeEnum(String type, String javaType) {
        this.type = type;
        this.javaType = javaType;
    }

    public static DbColumnTypeEnum get(String type) {
        for (DbColumnTypeEnum typeEnum : DbColumnTypeEnum.values()) {
            if (typeEnum.getType().equals(type)) {
                return typeEnum;
            }
        }
        return null;
    }

    public String getType() {
        return type;
    }

    public String getJavaType() {
        return javaType;
    }
}
