package com.patriclee.domain.vo;

import com.patriclee.domain.dto.ConversationItemDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "ConversationsResponse", description = "模型对话接口响应")
public class ConversationsVo {
    @Schema(description = "会话条目列表")
    private List<ConversationItemDto> conversations;
}
