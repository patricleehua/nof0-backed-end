package com.patriclee.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.patriclee.domain.entity.ConversationMessages;
import com.patriclee.service.ConversationMessagesService;
import com.patriclee.mapper.ConversationMessagesMapper;
import org.springframework.stereotype.Service;

/**
* @author leehua
* @description 针对表【conversation_messages(对话消息详情表)】的数据库操作Service实现
* @createDate 2025-11-09 16:53:48
*/
@Service
public class ConversationMessagesServiceImpl extends ServiceImpl<ConversationMessagesMapper, ConversationMessages>
    implements ConversationMessagesService{

}




