package com.patriclee.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 价格历史数据表
 * @TableName price_ticks
 */
@TableName(value ="price_ticks")
@Data
public class PriceTicks implements Serializable {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 数据源
     */
    private String provider;

    /**
     * 交易对
     */
    private String symbol;

    /**
     * 价格
     */
    private Double price;

    /**
     * 时间戳(毫秒)
     */
    private Long tsMs;

    /**
     * 成交量
     */
    private Double volume;

    /**
     * 原始数据
     */
    private Object raw;

    /**
     * 创建时间
     */
    private Date createdAt;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}