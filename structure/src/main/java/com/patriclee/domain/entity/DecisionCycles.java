package com.patriclee.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * AI决策周期表
 * @TableName decision_cycles
 */
@TableName(value ="decision_cycles")
@Data
public class DecisionCycles implements Serializable {
    /**
     * 决策周期ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 模型ID
     */
    private String modelId;

    /**
     * 周期编号
     */
    private Integer cycleNumber;

    /**
     * 提示摘要
     */
    private String promptDigest;

    /**
     * 思维链追踪
     */
    private String cotTrace;

    /**
     * 决策数据
     */
    private Object decisions;

    /**
     * 是否成功
     */
    private Integer success;

    /**
     * 错误信息
     */
    private String errorMessage;

    /**
     * 执行时间
     */
    private Date executedAt;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}