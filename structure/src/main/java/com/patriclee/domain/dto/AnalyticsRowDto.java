package com.patriclee.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "AnalyticsRow", description = "分析数据集合")
public class AnalyticsRowDto {
    @Schema(description = "费用与盈亏表")
    private AnalyticsTableDto feePnlMovesBreakdownTable;
    @Schema(description = "盈亏对比表")
    private AnalyticsTableDto winnersLosersBreakdownTable;
    @Schema(description = "信号分布表")
    private AnalyticsTableDto signalsBreakdownTable;
    @Schema(description = "整体交易概览表")
    private AnalyticsTableDto overallTradesOverviewTable;
    @Schema(description = "胜率")
    private double winRate;
    @Schema(description = "多空交易比")
    private double longShortTradesRatio;
    @Schema(description = "平均信心度")
    private double avgConfidence;
    @Schema(description = "置信度中位数")
    private double medianConfidence;
}
