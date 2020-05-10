package com.kalvin.kvf.modules.workflow.vo;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Create by Kalvin on 2020/4/22.
 */
@Data
@ToString
@Accessors(chain = true)
public class FormConfigVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;
    private String theme;
    private List<Field> fields;

    @Data
    @ToString
    public static class Field implements Serializable {
        private static final long serialVersionUID = 1L;
        private String id;
        private String fieldName;
        private String field;
        private Object value;
        private String control;
        private Integer required;
        private List<Option> options;
    }

    @Data
    @ToString
    public static class Option implements Serializable {
        private static final long serialVersionUID = 1L;
        private String id;
        private String fieldId;
        private String text;
        private String value;
    }

}
