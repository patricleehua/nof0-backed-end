package com.patriclee.ai.provider;


import com.patriclee.ai.retriever.ChatProviderAdvisorHelper;
import com.patriclee.config.AiProviderManager;
import com.patriclee.domain.dto.UnifiedChatRequest;
import com.patriclee.domain.dto.UnifiedChatResponse;
import com.patriclee.domain.entity.ModelInfo;
import com.patriclee.provider.ChatModelProvider;
import com.patriclee.req.Response;
import com.patriclee.utils.OptionExtractors;
import com.patriclee.utils.SentEventUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;

import java.util.*;

/**
 * OpenRouter Provider 实现.
 * <p>
 * OpenRouter 是一个 AI 模型聚合平台，支持多个厂商的模型：
 * - OpenAI (gpt-4o, gpt-3.5-turbo 等)
 * - Anthropic (claude-3-5-sonnet, claude-3-haiku 等)
 * - Google (gemini-2.0-flash-exp, gemini-pro 等)
 * - Meta (llama-3.1 系列)
 * - Mistral (mixtral, mistral 系列)
 * <p>
 * OpenRouter 完全兼容 OpenAI API 格式，只需要修改 baseUrl 和 apiKey。
 *
 * @author Nine Cloud Team
 * @since 2025/10/11
 */
@Slf4j
@Component("openRouterProvider")
@RequiredArgsConstructor
public class OpenRouterProvider implements ChatModelProvider {

    private final AiProviderManager providerManager;
    private final ChatProviderAdvisorHelper chatProviderAdvisorHelper;

    private static final String PROVIDER_NAME = "open-router";
    private static final String DEFAULT_BASE_URL = "https://openrouter.ai/api/v1";

    @Override
    public String getProviderName() {
        return PROVIDER_NAME;
    }

    @Override
    public String getDisplayName() {
        return "OpenRouter";
    }

    @Override
    public List<ModelInfo> getSupportedModels() {
        // 从配置中获取或使用默认列表
        List<String> configModels = providerManager.getAvailableModels(PROVIDER_NAME);
        if (!configModels.isEmpty()) {
            return configModels.stream().map(this::createModelInfo).toList();
        }

        // OpenRouter 常用模型列表（按厂商分类）
        return Arrays.asList(
            // OpenAI 系列
            createModelInfo("openai/gpt-4o", 0.005, "fast", "GPT-4 Omni", Arrays.asList("chat", "vision")),
            createModelInfo("openai/gpt-4o-mini", 0.00015, "fast", "GPT-4 Omni Mini", Arrays.asList("chat", "vision")),
            createModelInfo("openai/gpt-4-turbo", 0.01, "fast", "GPT-4 Turbo", Arrays.asList("chat", "vision")),
            createModelInfo("openai/gpt-3.5-turbo", 0.0005, "fast", "GPT-3.5 Turbo", Arrays.asList("chat")),

            // Anthropic 系列
            createModelInfo("anthropic/claude-3-5-sonnet", 0.003, "fast", "Claude 3.5 Sonnet", Arrays.asList("chat", "vision")),
            createModelInfo("anthropic/claude-3-haiku", 0.00025, "fast", "Claude 3 Haiku", Arrays.asList("chat")),
            createModelInfo("anthropic/claude-3-opus", 0.015, "medium", "Claude 3 Opus", Arrays.asList("chat", "vision")),

            // Google 系列
            createModelInfo("google/gemini-2.0-flash-exp", 0.0, "fast", "Gemini 2.0 Flash (Free)", Arrays.asList("chat", "vision")),
            createModelInfo("google/gemini-pro", 0.000125, "fast", "Gemini Pro", Arrays.asList("chat")),
            createModelInfo("google/gemini-pro-1.5", 0.00125, "fast", "Gemini Pro 1.5", Arrays.asList("chat", "vision")),

            // Meta Llama 系列
            createModelInfo("meta-llama/llama-3.1-8b-instruct:free", 0.0, "fast", "Llama 3.1 8B (Free)", Arrays.asList("chat")),
            createModelInfo("meta-llama/llama-3.1-70b-instruct", 0.00035, "fast", "Llama 3.1 70B", Arrays.asList("chat")),
            createModelInfo("meta-llama/llama-3.1-405b-instruct", 0.003, "medium", "Llama 3.1 405B", Arrays.asList("chat")),

            // Mistral 系列
            createModelInfo("mistralai/mistral-7b-instruct:free", 0.0, "fast", "Mistral 7B (Free)", Arrays.asList("chat")),
            createModelInfo("mistralai/mixtral-8x7b-instruct:nitro", 0.00024, "fast", "Mixtral 8x7B", Arrays.asList("chat")),
            createModelInfo("mistralai/mixtral-8x22b-instruct", 0.00065, "fast", "Mixtral 8x22B", Arrays.asList("chat"))
        );
    }

    @Override
    public Flux<ServerSentEvent<String>> chatStream(UnifiedChatRequest request, ChatClient chatClient) {
        try {
            log.debug("OpenRouter流式聊天开始 - 模型: {}, 消息: {}", request.getModel(), request.getMessage());

            return chatClient.prompt()
                .user(request.getMessage())
                .advisors(advisorSpec -> {
                    chatProviderAdvisorHelper.addChatHistoryAdvisor(advisorSpec, request.getSessionId(), request.getHistorySize());
                    chatProviderAdvisorHelper.addRagAdvisors(advisorSpec, request.getEnableRag(), request.getRagType(), request.getRagSearchConfig());
                })
                .stream()
                .content()
                .map(SentEventUtils::createMessageEvent)
                .concatWith(SentEventUtils.createCompleteEvent())
                .doOnComplete(() -> log.debug("OpenRouter流式聊天完成"))
                .doOnError(throwable -> {
                    log.error("OpenRouter流式聊天失败", throwable);
                })
                .onErrorResume(throwable -> SentEventUtils.createErrorEvent(throwable.getMessage()));

        } catch (Exception e) {
            log.error("OpenRouter流式聊天初始化失败", e);
            return SentEventUtils.createErrorEvent(e.getMessage());
        }
    }

    @Override
    public Response<UnifiedChatResponse> chatSync(UnifiedChatRequest request, ChatClient chatClient) {
        long startTime = System.currentTimeMillis();

        try {
            log.debug("OpenRouter同步聊天开始 - 模型: {}, 消息长度: {}", request.getModel(), request.getMessage().length());

            String responseContent = chatClient.prompt()
                .user(request.getMessage())
                .advisors(advisorSpec -> {
                    chatProviderAdvisorHelper.addChatHistoryAdvisor(advisorSpec, request.getSessionId(), request.getHistorySize());
                    chatProviderAdvisorHelper.addRagAdvisors(advisorSpec, request.getEnableRag(), request.getRagType(), request.getRagSearchConfig());
                })
                .call()
                .content();

            long duration = System.currentTimeMillis() - startTime;

            UnifiedChatResponse response = new UnifiedChatResponse();
            response.setMessageId(request.getMessageId());
            response.setSessionId(request.getSessionId());
            response.setContent(responseContent);
            response.setProvider(PROVIDER_NAME);
            response.setModel(request.getModel());
            response.setDuration(duration);
            response.setStatus("success");

            log.debug("OpenRouter同步聊天完成 - 响应长度: {}, 耗时: {}ms", responseContent.length(), duration);

            return Response.success(response);

        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            log.error("OpenRouter同步聊天失败: {}, 耗时: {}ms", e.getMessage(), duration, e);

            UnifiedChatResponse response = new UnifiedChatResponse();
            response.setStatus("failed");
            response.setErrorMessage(e.getMessage());
            return Response.fail(e.getMessage());
        }
    }

    @Override
    public ChatClient createChatClient(String model, Map<String, Object> optionOverrides) {
        try {
            Map<String, Object> overrides = optionOverrides != null
                ? new HashMap<>(optionOverrides)
                : Collections.emptyMap();
            String apiKey = providerManager.getApiKey(PROVIDER_NAME);
            String baseUrl = providerManager.getBaseUrl(PROVIDER_NAME);

            if (!StringUtils.hasText(apiKey)) {
                throw new IllegalArgumentException("OpenRouter API Key未配置");
            }

            if (!StringUtils.hasText(baseUrl)) {
                baseUrl = DEFAULT_BASE_URL;
            }

            if (!StringUtils.hasText(model)) {
                model = providerManager.getDefaultModel(PROVIDER_NAME);
                if (!StringUtils.hasText(model)) {
                    model = "openai/gpt-3.5-turbo";
                }
            }

            log.debug("创建OpenRouter ChatClient - BaseURL: {}, Model: {}, Overrides: {}", baseUrl, model, overrides);

            // OpenRouter 使用 OpenAI API 格式
            OpenAiApi api = OpenAiApi.builder()
                .apiKey(apiKey)
                .baseUrl(baseUrl)
                .build();

            OpenAiChatOptions options = OpenAiChatOptions.builder()
                .model(model)
                .build();

            OptionExtractors.extractDouble(overrides.get("temperature"))
                .ifPresent(options::setTemperature);
            OptionExtractors.extractInteger(overrides.get("maxTokens"))
                .ifPresent(options::setMaxTokens);

            OpenAiChatModel chatModel = OpenAiChatModel.builder()
                .openAiApi(api)
                .defaultOptions(options)
                .build();

            return ChatClient.create(chatModel);

        } catch (Exception e) {
            log.error("创建OpenRouter ChatClient失败", e);
            throw new RuntimeException("创建OpenRouter ChatClient失败: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean isAvailable() {
        return providerManager.isProviderAvailable(PROVIDER_NAME);
    }

    @Override
    public String healthCheck() {
        try {
            boolean available = isAvailable();
            int keyCount = providerManager.getApiKeyCount(PROVIDER_NAME);
            String baseUrl = providerManager.getBaseUrl(PROVIDER_NAME);
            return String.format("OpenRouter Provider: %s, API Keys: %d, BaseURL: %s",
                available ? "Available" : "Unavailable", keyCount, baseUrl != null ? baseUrl : DEFAULT_BASE_URL);
        } catch (Exception e) {
            return "OpenRouter Provider: Error - " + e.getMessage();
        }
    }

    private ModelInfo createModelInfo(String modelId) {
        ModelInfo info = new ModelInfo();
        info.setModelId(modelId);
        info.setDisplayName(modelId);
        info.setProvider(PROVIDER_NAME);
        info.setCostPer1kTokens(0.0005);
        info.setMaxTokens(4096);
        info.setSpeed("fast");
        info.setCapabilities(Arrays.asList("chat"));
        info.setEnabled(true);
        return info;
    }

    private ModelInfo createModelInfo(String modelId, double cost, String speed, String displayName, List<String> capabilities) {
        ModelInfo info = new ModelInfo();
        info.setModelId(modelId);
        info.setDisplayName(displayName);
        info.setProvider(PROVIDER_NAME);
        info.setCostPer1kTokens(cost);

        // 根据模型类型设置最大 tokens
        if (modelId.contains("gpt-4") || modelId.contains("claude") || modelId.contains("405b")) {
            info.setMaxTokens(8192);
        } else if (modelId.contains("gemini")) {
            info.setMaxTokens(32768);
        } else {
            info.setMaxTokens(4096);
        }

        info.setSpeed(speed);
        info.setCapabilities(capabilities);
        info.setEnabled(true);
        info.setDescription("OpenRouter " + displayName);
        return info;
    }

}
