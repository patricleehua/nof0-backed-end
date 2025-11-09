package com.patriclee.domain.vo;

import com.patriclee.domain.dto.AnalyticsRowDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "AnalyticsResponse", description = "分析接口响应")
public class AnalyticsVo {
    @Schema(description = "分析数据列表")
    private List<AnalyticsRowDto> analytics;
}
