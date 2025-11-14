package com.patriclee.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 提供商管理器，提供配置查询和密钥轮换逻辑.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AiProviderManager {

    private final AiProviderConfig aiProviderConfig;

    private final ConcurrentHashMap<String, AtomicInteger> keyCounters = new ConcurrentHashMap<>();

    /**
     * 获取指定提供商的配置.
     *
     * @param provider 提供商名称
     * @return 提供商配置，如果不存在或已禁用则返回null
     */
    public AiProviderConfig.ProviderConfig getProviderConfig(String provider) {
        if (aiProviderConfig.getProviders() == null) {
            log.warn("AI提供商配置为空");
            return null;
        }
        AiProviderConfig.ProviderConfig config = aiProviderConfig.getProviders().get(provider);
        if (config == null) {
            log.warn("提供商[{}]配置缺失", provider);
            return null;
        }
        if (Boolean.FALSE.equals(config.getEnabled())) {
            log.warn("提供商[{}]已禁用", provider);
            return null;
        }
        return config;
    }

    /**
     * 获取API密钥（支持轮换）.
     *
     * @param provider 提供商名称
     * @return API密钥，如果没有配置则返回null
     */
    public String getApiKey(String provider) {
        AiProviderConfig.ProviderConfig config = getProviderConfig(provider);
        if (config == null || CollectionUtils.isEmpty(config.getApiKeys())) {
            log.error("提供商[{}]未配置API密钥", provider);
            return null;
        }
        List<String> apiKeys = config.getApiKeys();
        if (apiKeys.size() == 1) {
            return apiKeys.get(0);
        }
        AtomicInteger counter = keyCounters.computeIfAbsent(provider, k -> new AtomicInteger(0));
        int index = Math.floorMod(counter.getAndIncrement(), apiKeys.size());
        String selectedKey = apiKeys.get(index);
        log.debug("提供商[{}]轮换到API密钥第{}个", provider, index + 1);
        return selectedKey;
    }

    /**
     * 获取指定索引的API密钥.
     *
     * @param provider 提供商名称
     * @param index    密钥索引
     * @return API密钥，如果索引越界或没有配置则返回null
     */
    public String getApiKey(String provider, int index) {
        AiProviderConfig.ProviderConfig config = getProviderConfig(provider);
        if (config == null || CollectionUtils.isEmpty(config.getApiKeys())) {
            log.error("提供商[{}]未配置API密钥", provider);
            return null;
        }
        List<String> apiKeys = config.getApiKeys();
        if (index < 0 || index >= apiKeys.size()) {
            log.error("提供商[{}]密钥索引[{}]超出范围，总数[{}]", provider, index, apiKeys.size());
            return null;
        }
        log.debug("提供商[{}]选择API密钥第{}个", provider, index + 1);
        return apiKeys.get(index);
    }

    /**
     * 获取基础URL.
     *
     * @param provider 提供商名称
     * @return 基础URL
     */
    public String getBaseUrl(String provider) {
        AiProviderConfig.ProviderConfig config = getProviderConfig(provider);
        return config != null ? config.getBaseUrl() : null;
    }

    /**
     * 获取默认模型.
     *
     * @param provider 提供商名称
     * @return 默认模型名称
     */
    public String getDefaultModel(String provider) {
        AiProviderConfig.ProviderConfig config = getProviderConfig(provider);
        return config != null ? config.getDefaultModel() : null;
    }

    /**
     * 获取超时时间.
     *
     * @param provider 提供商名称
     * @return 超时时间（毫秒），默认30000毫秒
     */
    public Integer getTimeout(String provider) {
        AiProviderConfig.ProviderConfig config = getProviderConfig(provider);
        return config != null ? config.getTimeout() : 30000;
    }

    /**
     * 获取最大重试次数.
     *
     * @param provider 提供商名称
     * @return 最大重试次数，默认3次
     */
    public Integer getMaxRetries(String provider) {
        AiProviderConfig.ProviderConfig config = getProviderConfig(provider);
        return config != null ? config.getMaxRetries() : 3;
    }

    /**
     * 检查提供商是否可用.
     *
     * @param provider 提供商名称
     * @return 是否可用（配置存在且有API密钥）
     */
    public boolean isProviderAvailable(String provider) {
        AiProviderConfig.ProviderConfig config = getProviderConfig(provider);
        return config != null && !CollectionUtils.isEmpty(config.getApiKeys());
    }

    /**
     * 获取API密钥数量.
     *
     * @param provider 提供商名称
     * @return API密钥数量
     */
    public int getApiKeyCount(String provider) {
        AiProviderConfig.ProviderConfig config = getProviderConfig(provider);
        return config != null && !CollectionUtils.isEmpty(config.getApiKeys())
            ? config.getApiKeys().size() : 0;
    }

    /**
     * 获取可用模型列表.
     *
     * @param provider 提供商名称
     * @return 可用模型列表，如果没有配置则返回空列表
     */
    public List<String> getAvailableModels(String provider) {
        AiProviderConfig.ProviderConfig config = getProviderConfig(provider);
        return config != null && !CollectionUtils.isEmpty(config.getAvailableModels())
            ? config.getAvailableModels() : new ArrayList<>();
    }

    /**
     * 重置密钥轮换计数器.
     *
     * @param provider 提供商名称
     */
    public void resetKeyCounter(String provider) {
        keyCounters.remove(provider);
        log.debug("提供商[{}]密钥轮换计数器已重置", provider);
    }
}