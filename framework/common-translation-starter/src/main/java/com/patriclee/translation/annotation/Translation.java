package com.patriclee.translation.annotation;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.patriclee.translation.handler.TranslationHandler;

import java.lang.annotation.*;

/**
 * 翻译注解
 * 用于标记需要进行翻译的字段
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
@Documented
@JacksonAnnotationsInside
@JsonSerialize(using = TranslationHandler.class)
public @interface Translation {

    /**
     * 翻译类型 (需与实现类上的 {@link TranslationType} 注解type对应)
     * 默认取当前字段的值，如果设置了 mapper 则取映射字段的值
     */
    String type();

    /**
     * 映射字段 (如果不为空则取此字段的值进行翻译)
     */
    String mapper() default "";

    /**
     * 其他条件 例如: 字典type(sys_user_sex)
     */
    String other() default "";
}