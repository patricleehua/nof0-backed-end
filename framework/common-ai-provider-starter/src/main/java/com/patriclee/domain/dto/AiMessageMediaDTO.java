package com.patriclee.domain.dto;


import lombok.Data;

/**
 * AI会话消息的媒体附件.
 */
@Data
public class AiMessageMediaDTO {

    /**
     * 媒体类型，如 image/png 或 audio/mpeg.
     */
    private String type;

    /**
     * 对象存储URL，返回给客户端时转换为临时URL.
     */
    private String url;

}
