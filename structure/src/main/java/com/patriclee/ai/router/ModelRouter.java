package com.patriclee.ai.router;


import com.patriclee.config.AiProviderManager;
import com.patriclee.domain.dto.UnifiedChatRequest;
import com.patriclee.provider.ChatModelProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * AI模型智能路由服务.
 *
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ModelRouter {

    private final Map<String, ChatModelProvider> providers; // Spring自动注入所有Provider
    private final AiProviderManager providerManager;

    /**
     * 选择最优Provider.
     *
     * @param request 统一请求对象
     * @return 选中的Provider
     */
    public ChatModelProvider selectProvider(UnifiedChatRequest request) {

        // 策略1: 用户指定provider
        if (request.getProvider() != null) {
            ChatModelProvider provider = providers.get(request.getProvider() + "Provider");
            if (provider != null && provider.isAvailable()) {
                log.debug("使用用户指定的Provider: {}", request.getProvider());
                return provider;
            }
            log.warn("指定的Provider [{}] 不可用,尝试自动路由", request.getProvider());
        }

        // 策略2: 根据model自动选择
        if (request.getModel() != null) {
            Optional<ChatModelProvider> providerByModel = findProviderByModel(request.getModel());
            if (providerByModel.isPresent()) {
                log.debug("根据模型 [{}] 自动选择Provider: {}", request.getModel(), providerByModel.get().getProviderName());
                return providerByModel.get();
            }
        }

        // 策略3: 智能路由
        ChatModelProvider intelligentProvider = intelligentRoute(request);
        if (intelligentProvider != null) {
            log.debug("智能路由选择Provider: {}", intelligentProvider.getProviderName());
            return intelligentProvider;
        }

        throw new RuntimeException("没有可用的AI Provider");
    }

    /**
     * 根据模型名称查找Provider.
     *
     * @param model 模型名称
     * @return Provider (可选)
     */
    private Optional<ChatModelProvider> findProviderByModel(String model) {
        return providers.values().stream()
            .filter(ChatModelProvider::isAvailable)
            .filter(provider -> provider.supportsModel(model))
            .findFirst();
    }

    /**
     * 智能路由算法.
     *
     * <p>优先级: 速度 > 成本 > 负载</p>
     *
     * @param request 请求对象
     * @return 选中的Provider
     */
    private ChatModelProvider intelligentRoute(UnifiedChatRequest request) {

        List<ChatModelProvider> availableProviders = providers.values().stream()
            .filter(ChatModelProvider::isAvailable)
            .toList();

        if (availableProviders.isEmpty()) {
            return null;
        }

        // 简单策略: 优先国内厂商 (速度快, 成本低)
        Optional<ChatModelProvider> dashscope = availableProviders.stream()
            .filter(p -> "dashscope".equals(p.getProviderName()))
            .findFirst();

        if (dashscope.isPresent()) {
            return dashscope.get();
        }

        // 降级: 返回第一个可用的
        ChatModelProvider fallback = availableProviders.get(0);
        log.warn("所有优先Provider不可用,降级到: {}", fallback.getProviderName());
        return fallback;
    }

    /**
     * 获取所有可用的Provider.
     *
     * @return Provider列表
     */
    public List<ChatModelProvider> getAvailableProviders() {
        return providers.values().stream()
            .filter(ChatModelProvider::isAvailable)
            .toList();
    }

    /**
     * 检查Provider是否可用.
     *
     * @param providerName Provider名称
     * @return true-可用, false-不可用
     */
    public boolean isProviderAvailable(String providerName) {
        ChatModelProvider provider = providers.get(providerName + "Provider");
        return provider != null && provider.isAvailable();
    }
}
