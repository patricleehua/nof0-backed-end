package com.patriclee.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.patriclee.domain.entity.Positions;
import com.patriclee.service.PositionsService;
import com.patriclee.mapper.PositionsMapper;
import org.springframework.stereotype.Service;

/**
* @author leehua
* @description 针对表【positions(持仓信息表)】的数据库操作Service实现
* @createDate 2025-11-09 16:53:48
*/
@Service
public class PositionsServiceImpl extends ServiceImpl<PositionsMapper, Positions>
    implements PositionsService{

}




