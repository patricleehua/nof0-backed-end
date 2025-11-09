package com.patriclee.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 交易员状态管理表
 * @TableName trader_state
 */
@TableName(value ="trader_state")
@Data
public class TraderState implements Serializable {
    /**
     * 交易员ID
     */
    @TableId
    private String traderId;

    /**
     * 交易所
     */
    private String exchangeProvider;

    /**
     * 市场数据提供商
     */
    private String marketProvider;

    /**
     * 资金分配百分比
     */
    private Double allocationPct;

    /**
     * 冷却期配置
     */
    private Object cooldown;

    /**
     * 风险控制
     */
    private Object riskGuards;

    /**
     * 最后决策时间
     */
    private Date lastDecisionAt;

    /**
     * 暂停直到
     */
    private Date pauseUntil;

    /**
     * 更新时间
     */
    private Date updatedAt;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}