package com.patriclee.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "PricePoint", description = "单个加密资产的报价信息")
public class PricePointDto {
    @Schema(description = "交易对符号，如 BTCUSDT")
    private String symbol;
    @Schema(description = "当前价格")
    private double price;
    @Schema(description = "毫秒时间戳")
    private long timestamp;
}
