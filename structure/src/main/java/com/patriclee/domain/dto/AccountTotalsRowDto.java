package com.patriclee.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "AccountTotalsRow", description = "账户权益快照及关联持仓")
public class AccountTotalsRowDto {
    @Schema(description = "模型 ID")
    private String modelId;
    @Schema(description = "账户 ID")
    private String id;
    @Schema(description = "快照时间戳（毫秒）")
    private long timestamp;
    @Schema(description = "账户净值")
    private double equity;
    @Schema(description = "账户资产价值")
    private double accountValue;
    @Schema(description = "已实现盈亏")
    private double realizedPnl;
    @Schema(description = "未实现盈亏")
    private double unrealizedPnl;
    @Schema(description = "区间收益率")
    private double returnPct;
    @Schema(description = "小时标记（增量拉取使用）")
    private long hourlyMarker;
    @Schema(description = "成立以来小时标记")
    private long sinceInceptionHourlyMarker;
    @Schema(description = "当前持仓列表")
    private List<PositionDto> positions;
}
