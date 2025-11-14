package com.patriclee.utils;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.codec.ServerSentEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

/**
 * SSE流式输出工具类
 * 用于统一管理Server-Sent Events的创建和格式化
 *
 * @author leehua
 * @since 2025/10/11
 */
@Slf4j
public class SentEventUtils {

    // ==================== 会话生命周期事件 ====================

    /**
     * 创建会话开始事件
     * @param conversationId 会话ID
     * @param messageId 用户消息ID
     * @return SSE事件
     */
    public static ServerSentEvent<String> createSessionStartEvent(String conversationId, String messageId) {
        JSONObject data = new JSONObject();
        data.put("conversationId", conversationId);
        data.put("messageId", messageId);
        data.put("timestamp", System.currentTimeMillis());
        return createSSEEvent("session_start", data.toJSONString());
    }

    /**
     * 创建会话结束事件
     * @return SSE事件的Mono包装
     */
    public static Mono<ServerSentEvent<String>> createCompleteEvent() {
        JSONObject data = new JSONObject();
        data.put("status", "COMPLETED");
        data.put("timestamp", System.currentTimeMillis());
        return Mono.just(createSSEEvent("complete", data.toJSONString()));
    }

    /**
     * 创建错误事件
     * @param errorMessage 错误消息
     * @param aiMessageId 消息ID(可选)
     * @return SSE事件的Flux包装
     */
    public static Flux<ServerSentEvent<String>> createErrorEvent(String errorMessage, String aiMessageId) {
        JSONObject data = new JSONObject();
        data.put("error", errorMessage);
        if (aiMessageId != null && !aiMessageId.isEmpty()) {
            data.put("aiMessageId", aiMessageId);
        }
        data.put("timestamp", System.currentTimeMillis());
        return Flux.just(createSSEEvent("error", data.toJSONString()));
    }

    /**
     * 创建错误事件(无消息ID)
     * @param errorMessage 错误消息
     * @return SSE事件的Flux包装
     */
    public static Flux<ServerSentEvent<String>> createErrorEvent(String errorMessage) {
        return createErrorEvent(errorMessage, null);
    }

    // ==================== 消息内容事件 ====================

    /**
     * 创建思考过程事件
     * 注意: data 直接为纯文本,不包装为 JSON,以提升流式输出性能
     * @param thinkingContent 思考内容
     * @return SSE事件
     */
    public static ServerSentEvent<String> createThinkingEvent(String thinkingContent) {
        return createSSEEvent("thinking", thinkingContent != null ? thinkingContent : "");
    }

    /**
     * 创建消息内容事件
     * 注意: data 直接为纯文本,不包装为 JSON,以提升流式输出性能
     * @param messageContent 消息内容
     * @return SSE事件
     */
    public static ServerSentEvent<String> createMessageEvent(String messageContent) {
        return createSSEEvent("message", messageContent != null ? messageContent : "");
    }

    // ==================== 工作流节点事件 ====================

    /**
     * 创建工作流节点事件
     * @param nodeName 节点名称
     * @param nodeType 节点类型
     * @param text 文本内容
     * @param thinking 思考内容
     * @param model 模型名称
     * @param nodeExecTime 节点执行时间
     * @return SSE事件
     */
    public static ServerSentEvent<String> createNodeEvent(String nodeName, String nodeType,
                                                          String text, String thinking,
                                                          String model, String nodeExecTime) {
        String data = createNodeResponseJson(thinking, text, Collections.emptyList(), model, nodeName, nodeExecTime);
        return createSSEEvent("node", data);
    }

    /**
     * 创建开始节点事件
     * @param nodeName 节点名称
     * @param nodeExecTime 执行时间
     * @return SSE事件
     */
    public static ServerSentEvent<String> createStartNodeEvent(String nodeName, String nodeExecTime) {
        String data = createNodeResponseJson("", "", Collections.emptyList(), "", nodeName, nodeExecTime);
        return createSSEEvent("start_node", data);
    }

    /**
     * 创建结束节点事件
     * @param nodeName 节点名称
     * @param result 结果内容
     * @param model 模型名称
     * @param nodeExecTime 执行时间
     * @return SSE事件
     */
    public static ServerSentEvent<String> createEndNodeEvent(String nodeName, String result,
                                                             String model, String nodeExecTime) {
        String data = createNodeResponseJson("", result, Collections.emptyList(), model, nodeName, nodeExecTime);
        return createSSEEvent("end_node", data);
    }

    /**
     * 创建检索节点事件(带文档列表)
     * @param nodeName 节点名称
     * @param chunkList 检索到的文档片段列表
     * @param model 模型名称
     * @param nodeExecTime 执行时间
     * @return SSE事件
     */
    public static ServerSentEvent<String> createRetrievalNodeEvent(String nodeName, List<?> chunkList,
                                                                   String model, String nodeExecTime) {
        String data = createNodeResponseJson("", "", chunkList, model, nodeName, nodeExecTime);
        return createSSEEvent("node", data);
    }

    // ==================== 文档引用事件 ====================

    /**
     * 创建文档引用事件
     * @param documents 文档列表
     * @return SSE事件
     */
    public static ServerSentEvent<String> createDocumentEvent(List<?> documents) {
        JSONObject data = new JSONObject();
        data.put("content", documents);
        return createSSEEvent("files", data.toJSONString());
    }

    // ==================== 通用SSE事件创建 ====================

    /**
     * 创建通用SSE事件
     * @param event 事件类型
     * @param data 事件数据
     * @return SSE事件
     */
    public static ServerSentEvent<String> createSSEEvent(String event, String data) {
        return ServerSentEvent.<String>builder()
            .data(data)
            .event(event)
            .build();
    }

    /**
     * 创建带ID的SSE事件
     * @param id 事件ID
     * @param event 事件类型
     * @param data 事件数据
     * @return SSE事件
     */
    public static ServerSentEvent<String> createSSEEventWithId(String id, String event, String data) {
        return ServerSentEvent.<String>builder()
            .id(id)
            .data(data)
            .event(event)
            .build();
    }

    // ==================== JSON数据格式化 ====================

    /**
     * 创建节点响应JSON
     * @param thinking 思考内容
     * @param text 文本内容
     * @param chunkList 文档片段列表
     * @param model 模型名称
     * @param nodeName 节点名称
     * @param nodeExecTime 执行时间
     * @return JSON字符串
     */
    public static String createNodeResponseJson(String thinking, String text, List<?> chunkList,
                                                String model, String nodeName, String nodeExecTime) {
        JSONObject json = new JSONObject();
        json.put("thinking", safeParse(thinking));
        json.put("text", safeParse(text));
        json.put("chunkList", chunkList != null ? chunkList : Collections.emptyList());
        json.put("model", model != null ? model : "");
        json.put("nodeName", nodeName != null ? nodeName : "");
        json.put("nodeExecTime", nodeExecTime != null ? nodeExecTime : "");
        return json.toJSONString();
    }

    /**
     * 创建简单响应JSON
     * @param content 内容
     * @return JSON字符串
     */
    public static String createSimpleResponseJson(String content) {
        JSONObject json = new JSONObject();
        json.put("content", content != null ? content : "");
        return json.toJSONString();
    }

    /**
     * 创建进度事件JSON
     * @param current 当前进度
     * @param total 总进度
     * @param message 进度消息
     * @return JSON字符串
     */
    public static String createProgressJson(int current, int total, String message) {
        JSONObject json = new JSONObject();
        json.put("current", current);
        json.put("total", total);
        json.put("message", message != null ? message : "");
        json.put("percentage", total > 0 ? (current * 100 / total) : 0);
        return json.toJSONString();
    }

    // ==================== 工具方法 ====================

    /**
     * 安全解析JSON字符串
     * 如果无法解析为JSON对象,则返回原字符串
     * @param value 输入字符串
     * @return 解析后的对象或原字符串
     */
    public static Object safeParse(String value) {
        if (value == null || value.trim().isEmpty()) {
            return "";
        }
        try {
            // 尝试解析为JSON对象
            return JSONObject.parseObject(value);
        } catch (Exception e) {
            // 解析失败,返回原字符串
            return value;
        }
    }

    /**
     * 转义JSON字符串中的特殊字符
     * @param value 输入字符串
     * @return 转义后的字符串
     */
    public static String escapeJson(String value) {
        if (value == null) {
            return "";
        }
        return value
            .replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace("\n", "\\n")
            .replace("\r", "\\r")
            .replace("\t", "\\t");
    }

    /**
     * 验证事件类型是否合法
     * @param eventType 事件类型
     * @return 是否合法
     */
    public static boolean isValidEventType(String eventType) {
        return eventType != null && !eventType.trim().isEmpty() &&
            eventType.matches("^[a-zA-Z0-9_-]+$");
    }

    // ==================== 常量定义 ====================

    /**
     * 事件类型常量
     */
    public static class EventType {
        public static final String SESSION_START = "session_start";
        public static final String COMPLETE = "complete";
        public static final String ERROR = "error";
        public static final String THINKING = "thinking";
        public static final String MESSAGE = "message";
        public static final String NODE = "node";
        public static final String START_NODE = "start_node";
        public static final String END_NODE = "end_node";
        public static final String FILES = "files";
        public static final String PROGRESS = "progress";
    }
}
