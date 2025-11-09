package com.patriclee.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.patriclee.domain.entity.Conversations;
import com.patriclee.service.ConversationsService;
import com.patriclee.mapper.ConversationsMapper;
import org.springframework.stereotype.Service;

/**
* @author leehua
* @description 针对表【conversations(对话记录表)】的数据库操作Service实现
* @createDate 2025-11-09 16:53:48
*/
@Service
public class ConversationsServiceImpl extends ServiceImpl<ConversationsMapper, Conversations>
    implements ConversationsService{

}




