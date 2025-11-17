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

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DataParsingNode implements NodeAction {
    private final ChatClient chatClient;
    private final String systemPrompt;

    public DataParsingNode(ChatClient chatClient, String systemPrompt) {
        this.chatClient = chatClient;
        this.systemPrompt = systemPrompt;
    }

    @Override
    public Map<String, Object> apply(OverAllState state) throws Exception {
        // 1. 从状态中获取所有交易数据
        String userInput = state.value("input", String.class).orElse("");
        String UserTradingPrompt = state.value("userTradingPrompt", String.class).orElse("");
        String historyAnalysis = state.value("historyAnalysis", String.class).orElse("");
        String news = state.value("news", String.class).orElse("");
        String sentiment = state.value("sentiment", String.class).orElse("");

        // 3. 使用 ChatClient 直接调用
        ChatResponse response = chatClient.prompt()
                .system(systemPrompt)
                .user(UserTradingPrompt)
                .call()
                .chatResponse();

        // 4. 提取结果
        String parsedData = "";
        String responseJson = "";
        String signal = "";

        if (response != null && response.getResult() != null) {
            AssistantMessage assistantMessage = response.getResult().getOutput();
            if (assistantMessage != null) {
                parsedData = assistantMessage.getText();
                JsonExtractionUtils.ExtractionResult extractionResult = JsonExtractionUtils.extract(parsedData);
                responseJson = extractionResult.jsonText();
                JsonNode extractedNode = extractionResult.jsonNode();
                if (extractedNode != null && extractedNode.has("signal")) {
                    signal = extractedNode.get("signal").asText("");
                }
            }
        }

        // 5. 返回解析结果、所有原始数据，以及交易建议
        Map<String, Object> result = new HashMap<>();
        result.put("parsedData", parsedData);
        result.put("originalInput", userInput);
        result.put("historyAnalysis", historyAnalysis);
        result.put("news", news);
        result.put("sentiment", sentiment);
        result.put("responseText", parsedData);
        result.put("responseJson", responseJson);
        result.put("signal", signal);

        return result;
    }
}
