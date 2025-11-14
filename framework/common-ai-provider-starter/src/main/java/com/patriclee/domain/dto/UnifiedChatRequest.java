package com.patriclee.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 统一AI聊天请求DTO.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "统一AI聊天请求")
public class UnifiedChatRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "会话ID", required = true)
    private String sessionId;

    @Schema(description = "用户消息内容", required = true)
    private String message;

    @Schema(description = "指定AI厂商 (openai/dashscope/azure/anthropic), 不填则自动选择")
    private String provider;

    @Schema(description = "指定模型 (gpt-4o/qwen-max等), 不填则使用厂商默认模型")
    private String model;

    @Schema(description = "是否流式响应", defaultValue = "true")
    private Boolean stream = true;

    @Schema(description = "历史对话轮数 (0表示不使用历史)", defaultValue = "10")
    private Integer historySize = 10;

    @Schema(description = "是否启用RAG检索增强")
    private Boolean enableRag = false;

    @Schema(description = "RAG检索类型 (local-本地向量检索/aliyun-云端向量检索/custom-自定义)", defaultValue = "local")
    private String ragType = "local";

    @Schema(description = "RAG索引ID列表")
    private List<String> ragIndexIds = new ArrayList<>();

    @Schema(description = "RAG搜索配置")
    private RemoteEmbeddingSearchDTO ragSearchConfig;

    @Schema(description = "多模态媒体文件列表 (图片/语音等)")
    private List<AiMessageMediaDTO> medias = new ArrayList<>();

    @Schema(description = "系统提示词")
    private String systemPrompt;

    @Schema(description = "温度参数 (0-1)", defaultValue = "1.0")
    private Float temperature = 1.0f;

    @Schema(description = "最大Token数")
    private Integer maxTokens;

    @Schema(description = "扩展元数据")
    private Map<String, Object> metadata = new HashMap<>();

    @Schema(description = "消息ID (更新消息时使用)")
    private String messageId;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "创建者ID")
    private String creatorId;
}
