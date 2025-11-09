package com.patriclee.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 账户权益快照表
 * @TableName account_equity_snapshots
 */
@TableName(value ="account_equity_snapshots")
@Data
public class AccountEquitySnapshots implements Serializable {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 模型ID
     */
    private String modelId;

    /**
     * 时间戳(毫秒)
     */
    private Long tsMs;

    /**
     * 美元权益
     */
    private Double dollarEquity;

    /**
     * 已实现盈亏
     */
    private Double realizedPnl;

    /**
     * 总未实现盈亏
     */
    private Double totalUnrealizedPnl;

    /**
     * 累计盈亏百分比
     */
    private Double cumPnlPct;

    /**
     * 夏普比率
     */
    private Double sharpeRatio;

    /**
     * 起始小时标记
     */
    private Integer sinceInceptionHourlyMarker;

    /**
     * 起始分钟标记
     */
    private Integer sinceInceptionMinuteMarker;

    /**
     * 元数据
     */
    private Object metadata;

    /**
     * 创建时间
     */
    private Date createdAt;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}