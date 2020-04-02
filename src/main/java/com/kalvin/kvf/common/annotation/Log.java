package com.kalvin.kvf.common.annotation;

import java.lang.annotation.*;

/** 
 * 【作用】系统操作日志注解<br>
 * 【说明】（无）
 * @author Kalvin
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {
    
    String value() default ""; // 操作说明
}

