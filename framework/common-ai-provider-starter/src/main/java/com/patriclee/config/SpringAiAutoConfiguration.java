package com.patriclee.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

/**
 * Spring AI自动配置类.
 */
@Slf4j
@AutoConfiguration
@EnableConfigurationProperties(AiProviderConfig.class)
@ComponentScan(basePackages = "com.patriclee.config")
public class SpringAiAutoConfiguration {

    /**
     * 创建AI提供商管理器Bean.
     */
    @Bean
    @ConditionalOnMissingBean
    public AiProviderManager aiProviderManager(AiProviderConfig aiProviderConfig) {
        log.info("初始化AI提供商管理器");
        return new AiProviderManager(aiProviderConfig);
    }
}