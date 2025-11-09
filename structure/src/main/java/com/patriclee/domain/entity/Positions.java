package com.patriclee.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 持仓信息表
 * @TableName positions
 */
@TableName(value ="positions")
@Data
public class Positions implements Serializable {
    /**
     * 持仓ID
     */
    @TableId
    private String id;

    /**
     * 模型ID
     */
    private String modelId;

    /**
     * 交易所
     */
    private String exchangeProvider;

    /**
     * 交易对
     */
    private String symbol;

    /**
     * 多空方向: long/short/flat
     */
    private String side;

    /**
     * 状态: open/closed
     */
    private String status;

    /**
     * 入场订单ID
     */
    private Long entryOid;

    /**
     * 风险金额(USD)
     */
    private Double riskUsd;

    /**
     * 置信度
     */
    private Double confidence;

    /**
     * 索引列
     */
    private Object indexCol;

    /**
     * 出场计划
     */
    private Object exitPlan;

    /**
     * 入场时间(毫秒)
     */
    private Long entryTimeMs;

    /**
     * 入场价格
     */
    private Double entryPrice;

    /**
     * 止盈订单ID
     */
    private Long tpOid;

    /**
     * 保证金
     */
    private Double margin;

    /**
     * 等待成交
     */
    private Integer waitForFill;

    /**
     * 止损订单ID
     */
    private Long slOid;

    /**
     * 当前价格
     */
    private Double currentPrice;

    /**
     * 已平仓盈亏
     */
    private Double closedPnl;

    /**
     * 强平价格
     */
    private Double liquidationPrice;

    /**
     * 手续费
     */
    private Double commission;

    /**
     * 杠杆
     */
    private Double leverage;

    /**
     * 滑点
     */
    private Double slippage;

    /**
     * 数量
     */
    private Double quantity;

    /**
     * 未实现盈亏
     */
    private Double unrealizedPnl;

    /**
     * 更新时间
     */
    private Date updatedAt;

    /**
     * 创建时间
     */
    private Date createdAt;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}