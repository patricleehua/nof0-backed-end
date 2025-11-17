package com.patriclee.workflow.node;

import com.alibaba.cloud.ai.graph.KeyStrategy;
import com.alibaba.cloud.ai.graph.KeyStrategyFactory;
import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.StateGraph;
import com.alibaba.cloud.ai.graph.action.AsyncNodeAction;
import com.alibaba.cloud.ai.graph.action.EdgeAction;
import com.alibaba.cloud.ai.graph.action.NodeAction;
import com.alibaba.cloud.ai.graph.agent.BaseAgent;
import com.alibaba.cloud.ai.graph.exception.GraphRunnerException;
import com.alibaba.cloud.ai.graph.exception.GraphStateException;
import com.alibaba.cloud.ai.graph.scheduling.ScheduleConfig;
import com.alibaba.cloud.ai.graph.scheduling.ScheduledAgentTask;
import com.alibaba.cloud.ai.graph.state.strategy.ReplaceStrategy;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.ToolCallback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.alibaba.cloud.ai.graph.action.AsyncNodeAction.node_async;
import static com.alibaba.cloud.ai.graph.action.AsyncEdgeAction.edge_async;
import static com.alibaba.cloud.ai.graph.StateGraph.START;

public class TradingAgent extends BaseAgent {

    private final ChatClient chatClient;
    private final List<ToolCallback> tradingTools;
    private final String dateParsingPrompt;
    private final String riskComplianceAssessmentPrompt;

    public TradingAgent(ChatClient chatClient, List<ToolCallback> tradingTools, String dateParsingPrompt, String riskComplianceAssessmentPrompt) throws GraphStateException {
        super("TradingAgent", "Trading agent for automated trading", "tradingResult");
        this.chatClient = chatClient;
        this.tradingTools = tradingTools;
        this.dateParsingPrompt = dateParsingPrompt;
        this.riskComplianceAssessmentPrompt = riskComplianceAssessmentPrompt;
    }

    @Override
    public AsyncNodeAction asAsyncNodeAction(String inputKeyFromParent, String outputKeyToParent) throws GraphStateException {
        // 创建一个包装了 TradingAgent 的 NodeAction
        NodeAction nodeAction = new NodeAction() {
            @Override
            public Map<String, Object> apply(OverAllState state) throws Exception {
                // 构建输入数据
                Map<String, Object> input = new HashMap<>();
                input.put("input", state.value("input", String.class).orElse(""));

                // 调用 TradingAgent 的 invoke 方法
                Optional<OverAllState> result = TradingAgent.this.invoke(input);

                // 返回结果
                if (result.isPresent()) {
                    return result.get().data();
                }

                return Map.of();
            }
        };

        return node_async(nodeAction);
    }

    @Override
    public ScheduledAgentTask schedule(ScheduleConfig scheduleConfig) throws GraphStateException, GraphRunnerException {
        return null;
    }

    @Override
    protected StateGraph initGraph() throws GraphStateException {
        return createNonStreamingTradingGraph();
    }

    /**
     * 创建非流式交易工作流Graph
     */
    private StateGraph createNonStreamingTradingGraph() throws GraphStateException {

        // 定义状态键策略
        KeyStrategyFactory keyStrategyFactory = () -> {
            HashMap<String, KeyStrategy> keyStrategyHashMap = new HashMap<>();
            // 添加相关的状态键和策略
            keyStrategyHashMap.put("tradingData", new ReplaceStrategy());
            keyStrategyHashMap.put("riskAssessment", new ReplaceStrategy());
            keyStrategyHashMap.put("tradingResult", new ReplaceStrategy());
            keyStrategyHashMap.put("marketData", new ReplaceStrategy());
            keyStrategyHashMap.put("accountInfo", new ReplaceStrategy());
            keyStrategyHashMap.put("shouldTrade", new ReplaceStrategy());
            keyStrategyHashMap.put("noTradeReason", new ReplaceStrategy());
            return keyStrategyHashMap;
        };

        // 1. 数据解析节点
        DataParsingNode dataParsingNode = new DataParsingNode(chatClient, dateParsingPrompt);

        // 2. 风险合规评估节点
        RiskComplianceAssessmentNode riskComplianceAssessmentNode = new RiskComplianceAssessmentNode(chatClient, riskComplianceAssessmentPrompt);

//        // 3. HyperLiquid 工具节点
//        HyperLiquidToolsNode hyperLiquidToolsNode = new HyperLiquidToolsNode(chatClient, tradingTools);
        TradeRecordingNode tradeRecordingNode = new TradeRecordingNode();
        // 4. 不交易记录节点
        NoTradeRecordingNode noTradeRecordingNode = new NoTradeRecordingNode();


        // 构建工作流（带条件判断）
        return new StateGraph("trading_workflow", keyStrategyFactory)
                // 添加所有节点
                .addNode("data_parsing", node_async(dataParsingNode))
                .addNode("risk_compliance_assessment", node_async(riskComplianceAssessmentNode))
//                .addNode("hyperliquid_tools", node_async(hyperLiquidToolsNode))
                .addNode("trade_recording", node_async(tradeRecordingNode))
                .addNode("no_trade_recording", node_async(noTradeRecordingNode))

                // 添加边
                .addEdge(START, "data_parsing")

                // 条件边：数据解析后判断是否交易
                .addConditionalEdges("data_parsing",
                        edge_async(new TradingDecisionEdgeAction()),
                        Map.of("continue", "risk_compliance_assessment",
                                "stop", "no_trade_recording"))

                // 条件边：数据解析后判断是否允许交易
                .addConditionalEdges("risk_compliance_assessment",
                        edge_async(new TradingApprovedEdgeAction()),
                        Map.of("approved", "trade_recording",
                                "rejected", "no_trade_recording"))

                .addEdge("no_trade_recording", StateGraph.END);
    }

    /**
     * 交易决策边动作 - 判断是否继续交易
     */
    private static class TradingDecisionEdgeAction implements EdgeAction {
        @Override
        public String apply(OverAllState state) throws Exception {
            // 从数据解析结果中获取交易建议
            String single = state.value("single", String.class).orElse("hold");

            // 如果建议不交易或明确说停止，则跳转到不交易记录节点
            if ("hold".equalsIgnoreCase(single)) {
                return "stop";
            }

            // 否则继续执行风险评估
            return "continue";
        }
    }
    /**
     * 不交易记录节点
     */
    private static class NoTradeRecordingNode implements NodeAction {
        @Override
        public Map<String, Object> apply(OverAllState state) throws Exception {
            String responseText = state.value("responseText", String.class).orElse("数据分析不建议交易");
            String single = state.value("single", String.class).orElse("hold");
            String riskStatus = state.value("riskStatus", String.class).orElse("rejected");
            String riskMessage = state.value("riskMessage", String.class).orElse("trading is not allowed");


            // 记录不交易的原因和最终结果
            Map<String, Object> result = new HashMap<>();
            result.put("responseText", responseText);
            result.put("single", single);
            result.put("riskStatus", riskStatus);
            result.put("riskMessage", riskMessage);

            // 也保留所有原始数据
            result.putAll(state.data());

            return result;
        }
    }
    /**
     * 交易记录节点
     */
    private static class TradeRecordingNode implements NodeAction {
        @Override
        public Map<String, Object> apply(OverAllState state) throws Exception {
            String responseText = state.value("responseText", String.class).orElse("Trading is executing");
            String single = state.value("single", String.class).orElse("hold");
            String riskStatus = state.value("riskStatus", String.class).orElse("rejected");
            String riskMessage = state.value("riskMessage", String.class).orElse("trading is not allowed");


            // 记录交易的原因和最终结果
            Map<String, Object> result = new HashMap<>();
            result.put("responseText", responseText);
            result.put("single", single);
            result.put("riskStatus", riskStatus);
            result.put("riskMessage", riskMessage);

            // 也保留所有原始数据
            result.putAll(state.data());

            return result;
        }
    }
    /**
     * 交易决策边动作 - 判断是否允许交易
     */
    private static class TradingApprovedEdgeAction implements EdgeAction {
        @Override
        public String apply(OverAllState state) throws Exception {
            // 从数据解析结果中获取交易建议
            String status = state.value("approved", String.class).orElse("rejected");

            // 如果建议不交易或明确说停止，则跳转到不交易记录节点
            if ("rejected".equalsIgnoreCase(status)) {
                return "stop";
            }

            // 否则继续执行风险评估
            return "approved";
        }
    }

}
