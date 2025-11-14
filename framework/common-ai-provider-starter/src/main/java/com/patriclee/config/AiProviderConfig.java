package com.patriclee.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * AI提供商的中央配置类.
 */
@ConfigurationProperties(prefix = "patriclee.ai.provider")
@Component
@Data
public class AiProviderConfig {

    /**
     * 按提供商代码分组的提供商配置.
     */
    private Map<String, ProviderConfig> providers;

    @Data
    public static class ProviderConfig {

        /**
         * 提供商基础URL.
         */
        private String baseUrl;

        /**
         * 用于轮换的API密钥集合.
         */
        private List<String> apiKeys;

        /**
         * 默认模型名称.
         */
        private String defaultModel;

        /**
         * 支持的模型列表.
         */
        private List<String> availableModels;

        /**
         * 请求超时时间（毫秒）.
         */
        private Integer timeout = 30000;

        /**
         * 最大重试次数.
         */
        private Integer maxRetries = 3;

        /**
         * 是否启用该提供商.
         */
        private Boolean enabled = true;

        /**
         * 可选的提供商元数据（请求头、额外参数等）.
         */
        private Map<String, String> metadata;
    }
}