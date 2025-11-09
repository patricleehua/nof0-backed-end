package com.patriclee.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 对话消息详情表
 * @TableName conversation_messages
 */
@TableName(value ="conversation_messages")
@Data
public class ConversationMessages implements Serializable {
    /**
     * 消息ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 对话ID
     */
    private Long conversationId;

    /**
     * 角色: system/user/assistant
     */
    private String role;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 时间戳(毫秒)
     */
    private Long tsMs;

    /**
     * 元数据
     */
    private Object metadata;

    /**
     * 创建时间
     */
    private Date createdAt;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}