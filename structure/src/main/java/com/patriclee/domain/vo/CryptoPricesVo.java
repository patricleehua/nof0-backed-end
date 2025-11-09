package com.patriclee.domain.vo;

import com.patriclee.domain.dto.PricePointDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "CryptoPricesResponse", description = "行情接口响应")
public class CryptoPricesVo {
    @Schema(description = "价格映射，key 为交易对")
    private Map<String, PricePointDto> prices;
    @Schema(description = "服务端时间（毫秒）")
    private long serverTime;
}
