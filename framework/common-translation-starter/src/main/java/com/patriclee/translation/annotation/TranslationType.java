package com.patriclee.translation.annotation;

import java.lang.annotation.*;

/**
 * 翻译类型注解
 * 用于标记翻译实现类的翻译类型
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
public @interface TranslationType {

    /**
     * 翻译类型标识
     */
    String type();
}