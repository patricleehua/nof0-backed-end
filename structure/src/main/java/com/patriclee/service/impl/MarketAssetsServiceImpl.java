package com.patriclee.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.patriclee.domain.entity.MarketAssets;
import com.patriclee.service.MarketAssetsService;
import com.patriclee.mapper.MarketAssetsMapper;
import org.springframework.stereotype.Service;

/**
* @author leehua
* @description 针对表【market_assets(市场资产元数据表)】的数据库操作Service实现
* @createDate 2025-11-09 16:53:48
*/
@Service
public class MarketAssetsServiceImpl extends ServiceImpl<MarketAssetsMapper, MarketAssets>
    implements MarketAssetsService{

}




