package com.patriclee.domain.vo;

import com.patriclee.domain.dto.TradeRowDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "TradesResponse", description = "成交接口响应")
public class TradesVo {
    @Schema(description = "成交记录数组")
    private List<TradeRowDto> trades;
}
