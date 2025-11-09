package com.patriclee.domain.vo;

import com.patriclee.domain.dto.AccountTotalsRowDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "AccountTotalsResponse", description = "账户汇总接口响应")
public class AccountTotalsVo {
    @Schema(description = "账户汇总数组")
    private List<AccountTotalsRowDto> accountTotals;
}
