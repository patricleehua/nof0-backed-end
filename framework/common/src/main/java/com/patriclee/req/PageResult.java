package com.patriclee.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageResult<T> implements Serializable {

    /**
     * 当前页码
     */
    private int current;
    /**
     * 每页显示的记录数
     */
    private int size;
    /**
     * 总记录数
     */
    private long total;
    /**
     * 总页数
     */
    private int totalPage;
    /**
     * 当前页数据集合
     */
    private List<T> records;

}
