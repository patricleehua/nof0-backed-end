//package com.patriclee.workflow.node;
//
//import com.alibaba.cloud.ai.graph.OverAllState;
//import com.alibaba.cloud.ai.graph.action.NodeAction;
//import org.springframework.ai.chat.client.ChatClient;
//import org.springframework.ai.chat.messages.AssistantMessage;
//import org.springframework.ai.chat.model.ChatResponse;
//import org.springframework.ai.tool.ToolCallback;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class HyperLiquidToolsNode implements NodeAction {
//
//    private final ChatClient chatClient;
//    private final List<ToolCallback> toolCallbacks;
//
//    public HyperLiquidToolsNode(ChatClient chatClient, List<ToolCallback> toolCallbacks) {
//        this.chatClient = chatClient;
//        this.toolCallbacks = toolCallbacks;
//    }
//
//    @Override
//    public Map<String, Object> apply(OverAllState state) throws Exception {
//        // 1. 获取所有数据
//        String riskAssessment = state.value("riskAssessment", String.class).orElse("");
//        String Account = state.value("account", String.class).orElse("");
//        String Kline = state.value("kline", String.class).orElse("");
//        String Indicators = state.value("indicators", String.class).orElse("");
//        String FundingRate = state.value("fundingRate", String.class).orElse("");
//        String Position = state.value("position", String.class).orElse("");
//        String Volume = state.value("volume", String.class).orElse("");
//        String HistoryAnalysis = state.value("historyAnalysis", String.class).orElse("");
//        String UserTradingPrompt = state.value("userTradingPrompt", String.class).orElse("");
//
//
//        String riskStatus = state.value("riskStatus", String.class).orElse("");
//        String riskMessage = state.value("riskMessage", String.class).orElse("");
//        String parsedData = state.value("parsedData", String.class).orElse("");
//
//
//        // 2. 构建工具调用提示
//        String systemPrompt = "你是一个专业的交易执行代理。基于所有市场数据和风险评估结果，执行相应的交易操作。";
//
//        // 构造完整的交易指令数据
//        String tradingDecisionData = String.format("""
//                Risk assessment results: %s
//                All market data:
//                Account Information: %s
//                Candlestick data: %s
//                Technical Indicators: %s
//                Funding rate: %s
//                Open Interest: %s
//                Volume: %s
//                Historical Analysis: %s
//                User Policy: %s
//                Please generate a specific trade execution order based on all the above information.
//            """, riskAssessment, Account, Kline, Indicators, FundingRate, Position, Volume, HistoryAnalysis, UserTradingPrompt);
//
//        // 3. 使用 ChatClient 生成交易指令
//        ChatResponse response = chatClient.prompt()
//                .system(systemPrompt)
//                .user(tradingDecisionData)
//                .call()
//                .chatResponse();
//
//        // 4. 提取结果
//        String tradingCommands = "";
//        if (response != null && response.getResult() != null) {
//            AssistantMessage assistantMessage = response.getResult().getOutput();
//            if (assistantMessage != null) {
//                tradingCommands = assistantMessage.getText();
//            }
//        }
//
//        // 5. 如果有工具，在这里执行
//        String toolExecutionResult = "";
//        if (toolCallbacks != null && !toolCallbacks.isEmpty()) {
//            // TODO: 执行具体的工具调用
//            // 这里需要根据具体的工具实现逻辑
//            // 例如：调用 HyperLiquid API 进行实际交易
//            toolExecutionResult = "工具调用完成: " + tradingCommands;
//        } else {
//            toolExecutionResult = "无工具配置，仅生成指令: " + tradingCommands;
//        }
//
//        // 6. 返回最终结果（包含所有数据）
//        Map<String, Object> result = new HashMap<>();
//        result.put("tradingCommands", tradingCommands);
//        result.put("tradingResult", toolExecutionResult);
//        result.put("executed", toolCallbacks != null && !toolCallbacks.isEmpty());
//        result.put("riskAssessment", riskAssessment);
//        result.put("account", Account);
//        result.put("kline", Kline);
//        result.put("indicators", Indicators);
//        result.put("fundingRate", FundingRate);
//        result.put("position", Position);
//        result.put("volume", Volume);
//        result.put("historyAnalysis", HistoryAnalysis);
//        result.put("userTradingPrompt", UserTradingPrompt);
//
//        return result;
//    }
//}
