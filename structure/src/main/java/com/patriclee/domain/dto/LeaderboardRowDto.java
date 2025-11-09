package com.patriclee.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "LeaderboardRow", description = "排行榜条目")
public class LeaderboardRowDto {
    @Schema(description = "模型 ID 或条目 ID")
    private String id;
    @Schema(description = "当前权益")
    private double equity;
    @Schema(description = "收益率")
    private double returnPct;
    @Schema(description = "交易次数")
    private int numTrades;
    @Schema(description = "获胜次数")
    private int numWins;
    @Schema(description = "亏损次数")
    private int numLosses;
    @Schema(description = "夏普比率")
    private double sharpe;
    @Schema(description = "累计盈利美元")
    private double winDollars;
    @Schema(description = "累计亏损美元")
    private double loseDollars;
}
