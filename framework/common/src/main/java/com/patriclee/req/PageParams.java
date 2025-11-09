package com.patriclee.req;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Data
public class PageParams {

    /**
     * 第几页
     */
    private int pageNo = 1;
    /**
     * 每页多少个
     */
    private int pageSize = 10;

    /**
     * 开始时间
     */
    private String beginTime;

    /**
     * 结束时间
     */
    private String endTime;

    /** 请求参数 */
    @Setter
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Map<String, Object> params;


    public Map<String, Object> getParams()
    {
        if (params == null)
        {
            params = new HashMap<>();
        }
        return params;
    }

}
