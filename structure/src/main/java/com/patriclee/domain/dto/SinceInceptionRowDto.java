package com.patriclee.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "SinceInceptionRow", description = "成立以来指标")
public class SinceInceptionRowDto {
    @Schema(description = "模型 ID")
    private String modelId;
    @Schema(description = "成立以来净值")
    private double navSinceInception;
    @Schema(description = "成立日期（毫秒）")
    private long inceptionDate;
    @Schema(description = "调用次数")
    private int numInvocations;
}
