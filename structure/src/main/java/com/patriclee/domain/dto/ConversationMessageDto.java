package com.patriclee.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "ConversationMessage", description = "对话消息")
public class ConversationMessageDto {
    @Schema(description = "角色 system/user/assistant")
    private String role;
    @Schema(description = "消息内容")
    private String content;
    @Schema(description = "时间戳（毫秒）")
    private long timestamp;
}
