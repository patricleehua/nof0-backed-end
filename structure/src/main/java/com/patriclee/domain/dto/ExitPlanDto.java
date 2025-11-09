package com.patriclee.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "ExitPlan", description = "仓位退出计划")
public class ExitPlanDto {
    @Schema(description = "目标盈利价格")
    private double profitTarget;
    @Schema(description = "止损价格")
    private double stopLoss;
    @Schema(description = "无效条件说明")
    private String invalidationCondition;
}
