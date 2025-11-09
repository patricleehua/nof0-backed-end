package com.patriclee.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 交易对信息表
 * @TableName symbols
 */
@TableName(value ="symbols")
@Data
public class Symbols implements Serializable {
    /**
     * 交易对符号
     */
    @TableId
    private String symbol;

    /**
     * 基础资产
     */
    private String baseAsset;

    /**
     * 计价资产
     */
    private String quoteAsset;

    /**
     * 基础资产精度
     */
    private Integer basePrecision;

    /**
     * 计价资产精度
     */
    private Integer quotePrecision;

    /**
     * 最小价格变动单位
     */
    private Double tickSize;

    /**
     * 元数据
     */
    private Object metadata;

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