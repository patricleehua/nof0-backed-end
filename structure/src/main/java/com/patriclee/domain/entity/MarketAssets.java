package com.patriclee.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 市场资产元数据表
 * @TableName market_assets
 */
@TableName(value ="market_assets")
@Data
public class MarketAssets implements Serializable {
    /**
     * 资产ID
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
     * 资产名称
     */
    private String name;

    /**
     * 规模小数位数
     */
    private Integer szDecimals;

    /**
     * 最大杠杆
     */
    private Double maxLeverage;

    /**
     * 仅隔离保证金
     */
    private Integer onlyIsolated;

    /**
     * 保证金表ID
     */
    private Integer marginTableId;

    /**
     * 是否下架
     */
    private Integer isDelisted;

    /**
     * 更新时间
     */
    private Date updatedAt;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}