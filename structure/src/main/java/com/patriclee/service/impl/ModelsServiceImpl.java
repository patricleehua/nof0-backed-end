package com.patriclee.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.patriclee.domain.entity.Models;
import com.patriclee.service.ModelsService;
import com.patriclee.mapper.ModelsMapper;
import org.springframework.stereotype.Service;

/**
* @author leehua
* @description 针对表【models(AI模型信息表)】的数据库操作Service实现
* @createDate 2025-11-09 16:53:48
*/
@Service
public class ModelsServiceImpl extends ServiceImpl<ModelsMapper, Models>
    implements ModelsService{

}




