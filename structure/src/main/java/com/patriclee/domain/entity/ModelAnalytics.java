package com.patriclee.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 模型分析数据表
 * @TableName model_analytics
 */
@TableName(value ="model_analytics")
@Data
public class ModelAnalytics implements Serializable {
    /**
     * 模型ID
     */
    @TableId
    private String modelId;

    /**
     * 分析结果数据
     */
    private Object payload;

    /**
     * 服务器时间(毫秒)
     */
    private Long serverTimeMs;

    /**
     * 更新时间
     */
    private Date updatedAt;

    /**
     * 元数据
     */
    private Object metadata;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}