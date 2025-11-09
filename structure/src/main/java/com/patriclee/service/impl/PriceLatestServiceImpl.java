package com.patriclee.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.patriclee.domain.entity.PriceLatest;
import com.patriclee.service.PriceLatestService;
import com.patriclee.mapper.PriceLatestMapper;
import org.springframework.stereotype.Service;

/**
* @author leehua
* @description 针对表【price_latest(最新价格缓存表)】的数据库操作Service实现
* @createDate 2025-11-09 16:53:48
*/
@Service
public class PriceLatestServiceImpl extends ServiceImpl<PriceLatestMapper, PriceLatest>
    implements PriceLatestService{

}




