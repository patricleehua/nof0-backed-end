package com.patriclee.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.patriclee.domain.entity.Symbols;
import com.patriclee.service.SymbolsService;
import com.patriclee.mapper.SymbolsMapper;
import org.springframework.stereotype.Service;

/**
* @author leehua
* @description 针对表【symbols(交易对信息表)】的数据库操作Service实现
* @createDate 2025-11-09 16:53:48
*/
@Service
public class SymbolsServiceImpl extends ServiceImpl<SymbolsMapper, Symbols>
    implements SymbolsService{

}




