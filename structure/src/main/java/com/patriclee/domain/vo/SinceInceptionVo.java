package com.patriclee.domain.vo;

import com.patriclee.domain.dto.SinceInceptionRowDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "SinceInceptionResponse", description = "成立以来指标响应")
public class SinceInceptionVo {
    @Schema(description = "服务器时间")
    private long serverTime;
    @Schema(description = "成立以来指标列表")
    private List<SinceInceptionRowDto> sinceInceptionValues;
}
