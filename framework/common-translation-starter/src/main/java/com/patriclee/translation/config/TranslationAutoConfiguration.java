package com.patriclee.translation.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.patriclee.translation.annotation.TranslationType;
import com.patriclee.translation.core.TranslationInterface;
import com.patriclee.translation.handler.TranslationBeanSerializerModifier;
import com.patriclee.translation.handler.TranslationHandler;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 翻译自动配置类
 * 用于初始化翻译组件和注册翻译实现
 */
@Slf4j
@AutoConfiguration
@ConditionalOnClass({TranslationInterface.class, ObjectMapper.class})
public class TranslationAutoConfiguration {

    @Autowired(required = false)
    private List<TranslationInterface<?>> translationList;

    @Autowired
    private ObjectMapper objectMapper;

    @PostConstruct
    public void init() {
        if (translationList == null || translationList.isEmpty()) {
            log.info("没有找到翻译实现类，翻译组件将不会生效");
            return;
        }

        Map<String, TranslationInterface<?>> translationMap = new HashMap<>(translationList.size());

        for (TranslationInterface<?> translation : translationList) {
            if (translation.getClass().isAnnotationPresent(TranslationType.class)) {
                TranslationType annotation = translation.getClass().getAnnotation(TranslationType.class);
                translationMap.put(annotation.type(), translation);
                log.info("注册翻译实现类: {} -> {}", annotation.type(), translation.getClass().getSimpleName());
            } else {
                log.warn("翻译实现类 {} 未标注 TranslationType 注解!", translation.getClass().getName());
            }
        }

        // 将翻译实现类映射关系注册到全局映射器
        TranslationHandler.TRANSLATION_MAPPER.putAll(translationMap);

        // 设置 Bean 序列化修改器
        objectMapper.setSerializerFactory(
            objectMapper.getSerializerFactory()
                .withSerializerModifier(new TranslationBeanSerializerModifier()));

        log.info("翻译组件初始化完成，共注册 {} 个翻译实现类", translationMap.size());
    }
}