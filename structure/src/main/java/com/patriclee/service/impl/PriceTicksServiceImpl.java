package com.patriclee.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.patriclee.domain.entity.PriceTicks;
import com.patriclee.service.PriceTicksService;
import com.patriclee.mapper.PriceTicksMapper;
import org.springframework.stereotype.Service;

/**
* @author leehua
* @description 针对表【price_ticks(价格历史数据表)】的数据库操作Service实现
* @createDate 2025-11-09 16:53:48
*/
@Service
public class PriceTicksServiceImpl extends ServiceImpl<PriceTicksMapper, PriceTicks>
    implements PriceTicksService{

}




