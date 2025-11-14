package com.patriclee.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 统一AI聊天响应DTO.
 */
@Data
@Schema(description = "统一AI聊天响应")
public class UnifiedChatResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "消息ID")
    private String messageId;

    @Schema(description = "会话ID")
    private String sessionId;

    @Schema(description = "AI响应内容")
    private String content;

    @Schema(description = "思考过程 (部分模型支持)")
    private String thinkingContent;

    @Schema(description = "实际使用的AI厂商")
    private String provider;

    @Schema(description = "实际使用的模型")
    private String model;

    @Schema(description = "响应耗时 (毫秒)")
    private Long duration;

    @Schema(description = "Token消耗数")
    private Integer tokens;

    @Schema(description = "成本 (元)")
    private Double cost;

    @Schema(description = "响应状态 (success/failed)")
    private String status;

    @Schema(description = "错误信息")
    private String errorMessage;

    @Schema(description = "扩展元数据")
    private Map<String, Object> metadata = new HashMap<>();
}
