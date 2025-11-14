package com.patriclee.ai.enums;

import com.baomidou.mybatisplus.annotation.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;


/**
 * AI消息状态
 */
@Getter
@AllArgsConstructor
public enum AiMessageStatusEnum implements IEnum<String> {

    INPROGRESS("inprogress"),
    SUCCESS("success"),
    FAIL("fail");
    private final String value;
    public static AiMessageStatusEnum valueOfAll(String value) {
        for (AiMessageStatusEnum aiMessageStatus : AiMessageStatusEnum.values()) {
            if(Objects.equals(value,aiMessageStatus.getValue())){
                return aiMessageStatus;
            }

        }
        return null;
    }
}
