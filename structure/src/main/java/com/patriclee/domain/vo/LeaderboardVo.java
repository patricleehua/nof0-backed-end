package com.patriclee.domain.vo;

import com.patriclee.domain.dto.LeaderboardRowDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "LeaderboardResponse", description = "排行榜接口响应")
public class LeaderboardVo {
    @Schema(description = "排行榜条目")
    private List<LeaderboardRowDto> leaderboard;
}
