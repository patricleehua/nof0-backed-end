package com.patriclee.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 交易账户信息表
 * @TableName accounts
 */
@TableName(value ="accounts")
@Data
public class Accounts implements Serializable {
    /**
     * 关联模型
     */
    @TableId
    private String modelId;

    /**
     * 交易所
     */
    private String exchangeProvider;

    /**
     * 账户标签
     */
    private String accountTag;

    /**
     * 保证金模式
     */
    private String marginMode;

    /**
     * 基础货币
     */
    private String baseCurrency;

    /**
     * 杠杆模式
     */
    private String leverageMode;

    /**
     * 创建时间
     */
    private Date createdAt;

    /**
     * 更新时间
     */
    private Date updatedAt;

    /**
     * 元数据
     */
    private Object metadata;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}