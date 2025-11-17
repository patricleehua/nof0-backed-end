package com.patriclee.controller;


import com.patriclee.workflow.AgentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
@Tag(name = "TEST", description = "TEST控制器")
public class TestController {
    private final AgentService agentService;

    @GetMapping("/execTrading")
    public String execTrading(@RequestParam String model, @RequestParam Integer type) {
        log.info("开始执行交易");
        boolean result = false;
        if(type == 1){
            result = agentService.execTrading(model);
        }else{
            result = agentService.execTradingStream(model);
        }
        log.info("交易执行结果: {}", result);
        return "交易执行结果: ";
    }
}
