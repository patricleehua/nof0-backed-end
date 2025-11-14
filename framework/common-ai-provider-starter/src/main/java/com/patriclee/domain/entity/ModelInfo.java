package com.patriclee.domain.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * AI模型信息.
 */
@Data
@Schema(description = "AI模型信息")
public class ModelInfo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "模型ID (如 gpt-4o, qwen-max)")
    private String modelId;

    @Schema(description = "模型显示名称")
    private String displayName;

    @Schema(description = "所属厂商")
    private String provider;

    @Schema(description = "每1k Token成本 (元)")
    private Double costPer1kTokens;

    @Schema(description = "最大Token数")
    private Integer maxTokens;

    @Schema(description = "响应速度 (fast/medium/slow)")
    private String speed;

    @Schema(description = "模型能力列表")
    private List<String> capabilities = new ArrayList<>();

    @Schema(description = "是否启用")
    private Boolean enabled = true;

    @Schema(description = "模型描述")
    private String description;
}
