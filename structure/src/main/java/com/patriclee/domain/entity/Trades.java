package com.patriclee.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 完整交易记录表
 * @TableName trades
 */
@TableName(value ="trades")
@Data
public class Trades implements Serializable {
    /**
     * 交易ID
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
     * 方向
     */
    private String side;

    /**
     * 交易类型
     */
    private String tradeType;

    /**
     * 交易ID
     */
    private String tradeId;

    /**
     * 数量
     */
    private Double quantity;

    /**
     * 杠杆
     */
    private Double leverage;

    /**
     * 置信度
     */
    private Double confidence;

    /**
     * 入场价格
     */
    private Double entryPrice;

    /**
     * 入场时间戳(毫秒)
     */
    private Long entryTsMs;

    /**
     * 入场可读时间
     */
    private String entryHumanTime;

    /**
     * 入场规模
     */
    private Double entrySz;

    /**
     * 入场交易ID
     */
    private Long entryTid;

    /**
     * 入场订单ID
     */
    private Long entryOid;

    /**
     * 入场是否交叉
     */
    private Integer entryCrossed;

    /**
     * 入场清算信息
     */
    private Object entryLiquidation;

    /**
     * 入场手续费(美元)
     */
    private Double entryCommissionDollars;

    /**
     * 入场已平仓盈亏
     */
    private Double entryClosedPnl;

    /**
     * 出场价格
     */
    private Double exitPrice;

    /**
     * 出场时间戳(毫秒)
     */
    private Long exitTsMs;

    /**
     * 出场可读时间
     */
    private String exitHumanTime;

    /**
     * 出场规模
     */
    private Double exitSz;

    /**
     * 出场交易ID
     */
    private Long exitTid;

    /**
     * 出场订单ID
     */
    private Long exitOid;

    /**
     * 出场是否交叉
     */
    private Integer exitCrossed;

    /**
     * 出场清算信息
     */
    private Object exitLiquidation;

    /**
     * 出场手续费(美元)
     */
    private Double exitCommissionDollars;

    /**
     * 出场已平仓盈亏
     */
    private Double exitClosedPnl;

    /**
     * 出场计划
     */
    private Object exitPlan;

    /**
     * 毛已实现盈亏
     */
    private Double realizedGrossPnl;

    /**
     * 净已实现盈亏
     */
    private Double realizedNetPnl;

    /**
     * 总手续费(美元)
     */
    private Double totalCommissionDollars;

    /**
     * 创建时间
     */
    private Date createdAt;

    /**
     * 更新时间
     */
    private Date updatedAt;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}