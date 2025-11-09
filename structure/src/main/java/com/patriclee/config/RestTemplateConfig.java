package com.patriclee.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * RestTemplate配置类.
 *
 * @author Patriclee
 * @since 2025/9/19
 */
@Configuration
public class RestTemplateConfig {


    /**
     * 配置RestTemplate Bean
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }



}
