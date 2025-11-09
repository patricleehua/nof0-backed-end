package com.patriclee.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.patriclee.domain.entity.Accounts;
import com.patriclee.service.AccountsService;
import com.patriclee.mapper.AccountsMapper;
import org.springframework.stereotype.Service;

/**
* @author leehua
* @description 针对表【accounts(交易账户信息表)】的数据库操作Service实现
* @createDate 2025-11-09 16:53:48
*/
@Service
public class AccountsServiceImpl extends ServiceImpl<AccountsMapper, Accounts>
    implements AccountsService{

}




