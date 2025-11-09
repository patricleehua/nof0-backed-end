package com.patriclee.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Position", description = "账户持仓详情")
public class PositionDto {
    @Schema(description = "持仓唯一标识")
    private String entryOid;
    @Schema(description = "交易标的")
    private String symbol;
    @Schema(description = "建仓价格")
    private double entryPrice;
    @Schema(description = "当前价格")
    private double currentPrice;
    @Schema(description = "持仓数量，正多负空")
    private double quantity;
    @Schema(description = "杠杆倍数")
    private double leverage;
    @Schema(description = "占用保证金")
    private double margin;
    @Schema(description = "风险敞口（USD）")
    private double riskUsd;
    @Schema(description = "模型置信度")
    private double confidence;
    @Schema(description = "退出计划")
    private ExitPlanDto exitPlan;
    @Schema(description = "建仓时间（毫秒）")
    private long entryTime;
    @Schema(description = "未实现盈亏")
    private Double unrealizedPnl;
    @Schema(description = "已实现盈亏（平仓后）")
    private Double closedPnl;
}
