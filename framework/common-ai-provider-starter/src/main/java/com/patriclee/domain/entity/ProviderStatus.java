package com.patriclee.domain.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * AI厂商状态.
 *
 */
@Data
@Schema(description = "AI厂商状态")
public class ProviderStatus implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "厂商名称")
    private String name;

    @Schema(description = "厂商显示名称")
    private String displayName;

    @Schema(description = "是否可用")
    private Boolean available;

    @Schema(description = "支持的模型数量")
    private Integer modelCount;

    @Schema(description = "API密钥数量")
    private Integer apiKeyCount;

    @Schema(description = "健康检查信息")
    private String healthMessage;
}
