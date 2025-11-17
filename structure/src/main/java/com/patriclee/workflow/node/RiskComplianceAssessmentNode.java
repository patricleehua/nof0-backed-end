package com.patriclee.workflow.node;

import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.action.NodeAction;
import com.fasterxml.jackson.databind.JsonNode;
import com.patriclee.utils.JsonExtractionUtils;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatResponse;

import java.util.HashMap;
import java.util.Map;

public class RiskComplianceAssessmentNode implements NodeAction {

    private final ChatClient chatClient;
    private final String systemPrompt;

    public RiskComplianceAssessmentNode(ChatClient chatClient, String systemPrompt) {
        this.chatClient = chatClient;
        this.systemPrompt = systemPrompt;
    }

    @Override
    public Map<String, Object> apply(OverAllState state) throws Exception {
        // 1. 获取所有数据
        String parsedData = state.value("parsedData", String.class).orElse("");
        String responseJson = state.value("responseJson", String.class).orElse("");
        String historyAnalysis = state.value("historyAnalysis", String.class).orElse("");
        String news = state.value("news", String.class).orElse("");
        String sentiment = state.value("sentiment", String.class).orElse("");

        String userInput = String.format("""
            交易请求:
            %s
            历史分析:
            %s
            市场情绪:
            %s
            新闻:
            %s
            请基于以上所有信息，判断是否允许交易。
            """, responseJson, historyAnalysis, news, sentiment);

        // 3. 使用 ChatClient 直接调用
        ChatResponse response = chatClient.prompt()
                .system(systemPrompt)
                .user(userInput)
                .call()
                .chatResponse();


        // 4. 提取结果
        String riskStatus = "";
        String riskMessage = "";
        if (response != null && response.getResult() != null) {
            AssistantMessage assistantMessage = response.getResult().getOutput();
            if (assistantMessage != null) {
                String assessment = assistantMessage.getText();
                JsonExtractionUtils.ExtractionResult extractionResult = JsonExtractionUtils.extract(assessment);
                JsonNode node = extractionResult.jsonNode();
                if (node != null && node.isObject()) {
                    riskStatus = node.path("status").asText("");
                    riskMessage = node.path("message").asText("");
                }
            }
        }

        // 5. 返回风险评估结果
        Map<String, Object> result = new HashMap<>();
        result.put("riskStatus", riskStatus);
        result.put("riskMessage", riskMessage);
        result.put("parsedData", parsedData);

        return result;
    }
}
