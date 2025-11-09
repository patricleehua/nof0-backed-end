package com.patriclee.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.patriclee.domain.entity.Trades;
import com.patriclee.service.TradesService;
import com.patriclee.mapper.TradesMapper;
import org.springframework.stereotype.Service;

/**
* @author leehua
* @description 针对表【trades(完整交易记录表)】的数据库操作Service实现
* @createDate 2025-11-09 16:53:48
*/
@Service
public class TradesServiceImpl extends ServiceImpl<TradesMapper, Trades>
    implements TradesService{

}




