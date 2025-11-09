package com.patriclee.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "ConversationItem", description = "模型会话记录")
public class ConversationItemDto {
    @Schema(description = "模型 ID")
    private String modelId;
    @Schema(description = "消息列表")
    private List<ConversationMessageDto> messages;
}
