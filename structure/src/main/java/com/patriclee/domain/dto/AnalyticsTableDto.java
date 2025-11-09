package com.patriclee.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "AnalyticsTable", description = "分析表格指标")
public class AnalyticsTableDto {
    @Schema(description = "表格标题")
    private String title;
    @Schema(description = "指标键值对")
    private Map<String, Double> metrics;
}
