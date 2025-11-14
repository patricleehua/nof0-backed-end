package com.patriclee.controller;


import com.patriclee.ai.enums.AiMessageStatusEnum;
import com.patriclee.ai.router.ModelRouter;
import com.patriclee.domain.dto.UnifiedChatRequest;
import com.patriclee.domain.dto.UnifiedChatResponse;
import com.patriclee.domain.entity.ModelInfo;
import com.patriclee.domain.entity.ProviderStatus;
import com.patriclee.provider.ChatModelProvider;
import com.patriclee.req.Response;
import com.patriclee.utils.SentEventUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 统一AI聊天控制器 - 对外统一接口.
 *
 */
@Slf4j
@RestController
@RequestMapping("/ai")
@Tag(name = "统一AI服务", description = "支持OpenAI/DashScope/Azure等多厂商")
@RequiredArgsConstructor
public class UnifiedAiChatController {

    private final ModelRouter modelRouter;

    /**
     * 流式AI聊天 - 统一接口.
     *
     * @param request 统一请求对象
     * @return SSE流
     */
    @PostMapping(value = "/chat/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(summary = "统一AI流式对话", description = "支持多厂商AI模型,自动路由到最优Provider")
    public Flux<ServerSentEvent<String>> chatStream(@RequestBody @Valid UnifiedChatRequest request) {
        String messageId = null;

        try {
            log.info("统一AI流式对话开始 - sessionId: {}, provider: {}, model: {}, messageId: {}",
                request.getSessionId(), request.getProvider(), request.getModel(), request.getMessageId());

            // 1. 路由选择Provider
            ChatModelProvider provider = modelRouter.selectProvider(request);
            if (provider == null) {
                return SentEventUtils.createErrorEvent("No available AI provider");
            }

            // 2. 确定模型
            String model = determineModel(request, provider);
            request.setModel(model);
            request.setProvider(provider.getProviderName());

            // 3. 保存会话和用户消息（支持重新发送） todo
            request.setMessageId(messageId);

            // 保存 messageId 到外层变量，用于错误处理
            String finalMessageId = messageId;

            // 4. 创建ChatClient
            Map<String, Object> optionOverrides = buildOptionOverrides(request);
            ChatClient chatClient = provider.createChatClient(model, optionOverrides);

            // 5. 执行流式对话
            StringBuffer fullResponse = new StringBuffer();
            StringBuffer thinkingResponse = new StringBuffer();

            // 6. 发送 session_start 事件，然后开始流式输出
            return Flux.just(SentEventUtils.createSessionStartEvent(request.getSessionId(), messageId))
                .concatWith(provider.chatStream(request, chatClient))
                .doOnNext(event -> {
                    // 累积完整响应
                    String eventType = event.event();
                    String eventData = event.data();

                    if (SentEventUtils.EventType.MESSAGE.equals(eventType)) {
                        fullResponse.append(eventData);
                    } else if (SentEventUtils.EventType.THINKING.equals(eventType)) {
                        thinkingResponse.append(eventData);
                    }
                })
                .doOnComplete(() -> {
                    // 流结束时保存AI响应 todo
                    try {
                        UnifiedChatResponse response = new UnifiedChatResponse();
                        response.setMessageId(finalMessageId);
                        response.setSessionId(request.getSessionId());
                        response.setContent(fullResponse.toString());
                        response.setThinkingContent(thinkingResponse.toString());
                        response.setProvider(provider.getProviderName());
                        response.setModel(model);
                        response.setStatus(AiMessageStatusEnum.SUCCESS.getValue());


                        // 更新用户消息状态为 success todo

                        log.info("统一AI流式对话完成 - sessionId: {}, messageId: {}", request.getSessionId(), finalMessageId);
                    } catch (Exception e) {
                        log.error("保存AI响应失败", e);
                        // 保存失败时也要更新用户消息状态为 fail todo
                    }
                })
                .doOnError(error -> {
                    log.error("统一AI流式对话失败 - messageId: {}, error: {}", finalMessageId, error.getMessage(), error);
                    // 失败时更新用户消息状态为 fail todo
                });

        } catch (Exception e) {
            log.error("统一AI流式对话初始化失败 - messageId: {}", messageId, e);
            // 初始化失败时也要更新用户消息状态 todo

            return SentEventUtils.createErrorEvent("初始化失败: " + e.getMessage(), messageId);
        }
    }

    /**
     * 同步AI聊天 - 统一接口.
     *
     * @param request 统一请求对象
     * @return 响应结果
     */
    @PostMapping("/chat/sync")
    @Operation(summary = "统一AI同步对话", description = "支持多厂商AI模型,自动路由到最优Provider")
    public Response<UnifiedChatResponse> chatSync(@RequestBody @Valid UnifiedChatRequest request) {

        try {
            log.info("统一AI同步对话开始 - sessionId: {}, provider: {}, model: {}",
                request.getSessionId(), request.getProvider(), request.getModel());

            // 1. 路由选择Provider
            ChatModelProvider provider = modelRouter.selectProvider(request);
            if (provider == null) {
                return Response.fail("No available AI provider");
            }

            // 2. 确定模型
            String model = determineModel(request, provider);
            request.setModel(model);
            request.setProvider(provider.getProviderName());

            // 3. 保存会话和用户消息 todo


            // 4. 创建ChatClient
            Map<String, Object> optionOverrides = buildOptionOverrides(request);
            ChatClient chatClient = provider.createChatClient(model, optionOverrides);

            // 5. 执行同步对话
            Response<UnifiedChatResponse> response = provider.chatSync(request, chatClient);
            // 6. 保存响应 todo


            return response;

        } catch (Exception e) {
            log.error("统一AI同步对话失败", e);
            return Response.fail(e.getMessage());
        }
    }

    private Map<String, Object> buildOptionOverrides(UnifiedChatRequest request) {
        Map<String, Object> overrides = new HashMap<>();
        if (request.getTemperature() != null) {
            overrides.put("temperature", request.getTemperature());
        }
        if (request.getMaxTokens() != null) {
            overrides.put("maxTokens", request.getMaxTokens());
        }
        Map<String, Object> metadata = request.getMetadata();
        if (metadata != null) {
            Object optionOverrides = metadata.get("optionOverrides");
            if (optionOverrides instanceof Map<?, ?> map) {
                map.forEach((key, value) -> overrides.put(String.valueOf(key), value));
            }
        }
        return overrides.isEmpty() ? Collections.emptyMap() : overrides;
    }

    /**
     * 获取所有可用模型.
     *
     * @return 模型列表 (按厂商分组)
     */
    @GetMapping("/models")
    @Operation(summary = "获取所有可用模型", description = "返回按厂商分组的模型列表")
    public Response<Map<String, List<ModelInfo>>> getAvailableModels() {
        try {
            Map<String, List<ModelInfo>> modelsMap = new HashMap<>();

            List<ChatModelProvider> providers = modelRouter.getAvailableProviders();
            for (ChatModelProvider provider : providers) {
                modelsMap.put(provider.getProviderName(), provider.getSupportedModels());
            }

            return Response.success(modelsMap);

        } catch (Exception e) {
            log.error("获取模型列表失败", e);
            return Response.fail(e.getMessage());
        }
    }

    /**
     * 获取所有厂商状态.
     *
     * @return 厂商状态列表
     */
    @GetMapping("/providers")
    @Operation(summary = "获取所有AI厂商状态", description = "返回所有配置的AI厂商及其状态")
    public Response<List<ProviderStatus>> getProviderStatus() {
        try {
            List<ChatModelProvider> providers = modelRouter.getAvailableProviders();

            List<ProviderStatus> statusList = providers.stream()
                .map(provider -> {
                    ProviderStatus status = new ProviderStatus();
                    status.setName(provider.getProviderName());
                    status.setDisplayName(provider.getDisplayName());
                    status.setAvailable(provider.isAvailable());
                    status.setModelCount(provider.getSupportedModels().size());
                    status.setHealthMessage(provider.healthCheck());
                    return status;
                })
                .collect(Collectors.toList());

            return Response.success(statusList);

        } catch (Exception e) {
            log.error("获取厂商状态失败", e);
            return Response.fail(e.getMessage());
        }
    }

    /**
     * 健康检查.
     *
     * @return 健康状态
     */
    @GetMapping("/health")
    @Operation(summary = "健康检查", description = "检查统一AI服务是否正常")
    public Response<Map<String, Object>> health() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("availableProviders", modelRouter.getAvailableProviders().size());
        health.put("timestamp", System.currentTimeMillis());
        return Response.success(health);
    }

    /**
     * 确定使用的模型.
     *
     * @param request  请求对象
     * @param provider Provider实例
     * @return 模型名称
     */
    private String determineModel(UnifiedChatRequest request, ChatModelProvider provider) {
        if (request.getModel() != null && provider.supportsModel(request.getModel())) {
            return request.getModel();
        }

        // 使用Provider的默认模型
        List<ModelInfo> models = provider.getSupportedModels();
        if (!models.isEmpty()) {
            return models.get(0).getModelId();
        }

        throw new IllegalArgumentException("Provider has no available models");
    }
}
