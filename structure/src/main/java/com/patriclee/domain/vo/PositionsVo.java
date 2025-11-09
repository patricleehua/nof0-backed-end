package com.patriclee.domain.vo;

import com.patriclee.domain.dto.PositionDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "PositionsResponse", description = "持仓接口响应")
public class PositionsVo {
    @Schema(description = "持仓列表")
    private List<PositionDto> positions;
}
