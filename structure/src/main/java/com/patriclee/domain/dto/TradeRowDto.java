package com.patriclee.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "TradeRow", description = "成交记录数据")
public class TradeRowDto {
    @Schema(description = "成交唯一 ID")
    private String id;
    @Schema(description = "模型 ID")
    private String modelId;
    @Schema(description = "标的符号")
    private String symbol;
    @Schema(description = "方向 LONG/SHORT")
    private String side;
    @Schema(description = "建仓价格")
    private double entryPrice;
    @Schema(description = "平仓价格")
    private double exitPrice;
    @Schema(description = "成交数量")
    private double quantity;
    @Schema(description = "杠杆")
    private double leverage;
    @Schema(description = "建仓时间")
    private long entryTime;
    @Schema(description = "平仓时间")
    private long exitTime;
    @Schema(description = "建仓时间（人类可读）")
    private String entryHumanTime;
    @Schema(description = "平仓时间（人类可读）")
    private String exitHumanTime;
    @Schema(description = "净盈亏")
    private double realizedNetPnl;
    @Schema(description = "毛盈亏")
    private double realizedGrossPnl;
    @Schema(description = "手续费（USD）")
    private double totalCommissionDollars;
}
