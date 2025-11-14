package com.patriclee.provider;


import com.patriclee.domain.dto.UnifiedChatRequest;
import com.patriclee.domain.dto.UnifiedChatResponse;
import com.patriclee.domain.entity.ModelInfo;
import com.patriclee.req.Response;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.codec.ServerSentEvent;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

/**
 * AI模型Provider接口 - 所有AI厂商需实现此接口.
 */
public interface ChatModelProvider {

    /**
     * 获取Provider名称.
     *
     * @return Provider名称 (如 "openai", "dashscope")
     */
    String getProviderName();

    /**
     * 获取Provider显示名称.
     *
     * @return 显示名称 (如 "OpenAI", "阿里云百炼")
     */
    default String getDisplayName() {
        return getProviderName();
    }

    /**
     * 获取支持的模型列表.
     *
     * @return 模型信息列表
     */
    List<ModelInfo> getSupportedModels();

    /**
     * 流式聊天.
     *
     * @param request    统一请求对象
     * @param chatClient Spring AI ChatClient
     * @return SSE流
     */
    Flux<ServerSentEvent<String>> chatStream(UnifiedChatRequest request, ChatClient chatClient);

    /**
     * 同步聊天.
     *
     * @param request    统一请求对象
     * @param chatClient Spring AI ChatClient
     * @return 响应结果
     */
    Response<UnifiedChatResponse> chatSync(UnifiedChatRequest request, ChatClient chatClient);

    /**
     * 创建ChatClient实例（带可扩展参数覆盖）.
     *
     * @param model            模型名称
     * @param optionOverrides  提供商特定参数
     * @return ChatClient实例
     */
    ChatClient createChatClient(String model, Map<String, Object> optionOverrides);


    /**
     * 检查Provider是否可用.
     *
     * @return true-可用, false-不可用
     */
    boolean isAvailable();

    /**
     * 检查模型是否支持.
     *
     * @param model 模型名称
     * @return true-支持, false-不支持
     */
    default boolean supportsModel(String model) {
        return getSupportedModels().stream()
            .anyMatch(m -> m.getModelId().equals(model));
    }

    /**
     * 获取模型信息.
     *
     * @param modelId 模型ID
     * @return 模型信息
     */
    default ModelInfo getModelInfo(String modelId) {
        return getSupportedModels().stream()
            .filter(m -> m.getModelId().equals(modelId))
            .findFirst()
            .orElse(null);
    }

    /**
     * 健康检查.
     *
     * @return 健康信息
     */
    default String healthCheck() {
        return isAvailable() ? "OK" : "Provider not available";
    }
}
