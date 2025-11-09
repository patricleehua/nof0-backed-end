package com.patriclee.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 市场资产上下文表
 * @TableName market_asset_ctx
 */
@TableName(value ="market_asset_ctx")
@Data
public class MarketAssetCtx implements Serializable {
    /**
     * 上下文ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 数据提供商
     */
    private String provider;

    /**
     * 交易对
     */
    private String symbol;

    /**
     * 资金费率
     */
    private Double funding;

    /**
     * 未平仓量
     */
    private Double openInterest;

    /**
     * 预言机价格
     */
    private Double oraclePx;

    /**
     * 标记价格
     */
    private Double markPx;

    /**
     * 中间价格
     */
    private Double midPx;

    /**
     * 影响价格
     */
    private Object impactPxs;

    /**
     * 前一日价格
     */
    private Double prevDayPx;

    /**
     * 日名义交易量
     */
    private Double dayNtlVlm;

    /**
     * 日基础交易量
     */
    private Double dayBaseVlm;

    /**
     * 更新时间
     */
    private Date updatedAt;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}