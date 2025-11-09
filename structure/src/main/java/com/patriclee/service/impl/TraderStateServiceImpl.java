package com.patriclee.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.patriclee.domain.entity.TraderState;
import com.patriclee.service.TraderStateService;
import com.patriclee.mapper.TraderStateMapper;
import org.springframework.stereotype.Service;

/**
* @author leehua
* @description 针对表【trader_state(交易员状态管理表)】的数据库操作Service实现
* @createDate 2025-11-09 16:53:48
*/
@Service
public class TraderStateServiceImpl extends ServiceImpl<TraderStateMapper, TraderState>
    implements TraderStateService{

}




