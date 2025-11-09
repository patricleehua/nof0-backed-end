package com.patriclee.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 最新价格缓存表
 * @TableName price_latest
 */
@TableName(value ="price_latest")
@Data
public class PriceLatest implements Serializable {
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
     * 原始数据
     */
    private Object raw;

    /**
     * 更新时间
     */
    private Date updatedAt;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}