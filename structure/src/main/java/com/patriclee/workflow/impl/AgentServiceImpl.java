package com.patriclee.workflow.impl;

import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.exception.GraphRunnerException;
import com.alibaba.cloud.ai.graph.exception.GraphStateException;
import com.patriclee.ai.router.ModelRouter;
import com.patriclee.domain.dto.UnifiedChatRequest;
import com.patriclee.provider.ChatModelProvider;
import com.patriclee.utils.PlaceHoldersReplacingBuildingUtils;
import com.patriclee.workflow.AgentService;
import com.patriclee.workflow.node.TradingAgent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AgentServiceImpl implements AgentService {

    @Autowired
    private ModelRouter modelRouter;

    @Value("classpath:prompts/DataParsingPrompt.st")
    private Resource DataParsingPrompt;
    @Value("classpath:prompts/RiskComplianceAssessmentPrompt.st")
    private Resource RiskComplianceAssessmentPrompt;

    private static final String User_TRADING_PROMPT_TEMPLATE_PATH = "template/UserTradingPromptTemplate.st";
    private static final String COIN_TEMPLATE_PATH = "template/CoinMarketSections.st";


    private static final List<CoinSeed> DEFAULT_COIN_DATA = List.of(
            new CoinSeed("BTC", 68000, 67500, -120, 55, 1.5, 1.2, 0.02, 89, 75),
            new CoinSeed("ETH", 3900, 3850, 42, 61, 0.72, 0.65, 0.01, 45, 40),
            new CoinSeed("SOL", 170, 168, 12, 58, 0.18, 0.15, 0.015, 12, 9),
            new CoinSeed("BNB", 600, 598, 6, 52, 0.25, 0.21, 0.008, 6, 5),
            new CoinSeed("DOGE", 0.18, 0.17, -0.5, 48, 0.05, 0.04, 0.005, 3, 2),
            new CoinSeed("XRP", 0.62, 0.61, 1.4, 50, 0.12, 0.10, 0.007, 4, 3)
    );

    /**
     * 执行交易代理（定时触发）
     *
     * @param model 模型名称
     * @return 执行结果
     */
    @Override
    public boolean execTrading(String model) {
        try {
            // 1. 获取模型提供商
            ChatModelProvider openRouter = modelRouter.selectProvider(
                    UnifiedChatRequest.builder()
                            .provider("openRouter")
                            .build());

            // 2. 创建对话客户端
            ChatClient chatClient = openRouter.createChatClient(model, null);

            // 3. 获取交易工具（如果需要 MCP 工具或其他工具，在这里配置）
            List<ToolCallback> tradingTools = List.of(); // TODO: 添加实际的交易工具

            // ========== 数据获取阶段 ==========
            List<Map<String, Object>> coinMarketData = fetchCoinMarketData();
            PerformanceMetrics performanceMetrics = fetchPerformanceMetrics();
            AccountSnapshot accountSnapshot = fetchAccountSnapshot();
            PositionSnapshot positionSnapshot = fetchPositionSnapshot();
            int minutesElapsed = fetchMinutesElapsed();

            // 2. 获取 K线、指标数据 - 3minutes K线 / 4 hours 指标
            String Kline = fetchKlineData(coinMarketData);
            String Indicators = fetchIndicatorsData(coinMarketData);

            // 3. 获取资金费率/未平仓量 / 成交量
            String FundingRate = fetchFundingRate(coinMarketData);
            String Position = fetchPosition(positionSnapshot);
            String Volume = fetchVolume(coinMarketData);

            // 5. 获取账户状态/持仓
            String Account = fetchAccount(accountSnapshot);

            // 5.1. 获取历史分析策略+结果
            String HistoryAnalysis = fetchHistoryAnalysis();

            // 6. 构造 UserPrompt
            String userPrompt = "用户提示词，非系统提示词";

            // 7. 获取用户自定义交易策略
            String UserTradingPrompt = fetchUserTradingPrompt(
                    coinMarketData,
                    performanceMetrics,
                    accountSnapshot,
                    positionSnapshot,
                    minutesElapsed);

            // ========== 准备输入数据 ==========
            Map<String, Object> input = new HashMap<>();
            input.put("input", userPrompt);
            input.put("kline", Kline);
            input.put("indicators", Indicators);
            input.put("fundingRate", FundingRate);
            input.put("position", Position);
            input.put("volume", Volume);
            input.put("account", Account);
            input.put("historyAnalysis", HistoryAnalysis);
            input.put("userTradingPrompt", UserTradingPrompt);

            // ========== 执行交易工作流 ==========

            // 4. 创建交易代理
            TradingAgent tradingAgent = new TradingAgent(chatClient, tradingTools,DataParsingPrompt.toString(),RiskComplianceAssessmentPrompt.toString());

            // 6. 执行交易工作流
            Optional<OverAllState> result = tradingAgent.invoke(input);

            // 7. 处理结果
            if (result.isPresent()) {
                log.info("交易执行成功: {}", result.get());
                return true;
            } else {
                log.warn("交易执行未返回结果");
                return false;
            }

        } catch (GraphStateException e) {
            log.error("图状态异常: {}", e.getMessage(), e);
            return false;
        } catch (GraphRunnerException e) {
            log.error("图执行异常: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        } catch (Exception e) {
            log.error("执行交易时发生未知异常: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * 执行交易（流式版本）
     *
     * @param model 模型名称
     * @return 执行结果
     */
    public boolean execTradingStream(String model) {
        try {
            // 1. 获取模型提供商
            ChatModelProvider openRouter = modelRouter.selectProvider(
                    UnifiedChatRequest.builder()
                            .provider("openRouter")
                            .build());

            // 2. 创建对话客户端
            ChatClient chatClient = openRouter.createChatClient(model, null);

            // 3. 获取交易工具
            List<ToolCallback> tradingTools = List.of(); // TODO: 添加实际的交易工具

            // ========== 数据获取阶段（流式也需要数据）==========
            List<Map<String, Object>> coinMarketData = fetchCoinMarketData();
            PerformanceMetrics performanceMetrics = fetchPerformanceMetrics();
            AccountSnapshot accountSnapshot = fetchAccountSnapshot();
            PositionSnapshot positionSnapshot = fetchPositionSnapshot();
            int minutesElapsed = fetchMinutesElapsed();

            String Kline = fetchKlineData(coinMarketData);
            String Indicators = fetchIndicatorsData(coinMarketData);
            String FundingRate = fetchFundingRate(coinMarketData);
            String Position = fetchPosition(positionSnapshot);
            String Volume = fetchVolume(coinMarketData);
            String Account = fetchAccount(accountSnapshot);
            String HistoryAnalysis = fetchHistoryAnalysis();
            String UserTradingPrompt = fetchUserTradingPrompt(
                    coinMarketData,
                    performanceMetrics,
                    accountSnapshot,
                    positionSnapshot,
                    minutesElapsed);

            // ========== 构建用户提示 ==========
            String userPrompt = "用户提示词，非系统提示词";

            // ========== 准备输入数据 ==========
            Map<String, Object> input = new HashMap<>();
            input.put("input", userPrompt);
            input.put("kline", Kline);
            input.put("indicators", Indicators);
            input.put("fundingRate", FundingRate);
            input.put("position", Position);
            input.put("volume", Volume);
            input.put("account", Account);
            input.put("historyAnalysis", HistoryAnalysis);
            input.put("userTradingPrompt", UserTradingPrompt);

            // 4. 创建交易代理（使用提示参数）
            TradingAgent tradingAgent = new TradingAgent(chatClient, tradingTools, DataParsingPrompt.toString(),RiskComplianceAssessmentPrompt.toString());

            // 6. 流式执行交易
            tradingAgent.stream(input)
                    .doOnNext(output -> log.info("流式输出: {}", output))
                    .doOnError(error -> log.error("流式执行错误: {}", error.getMessage()))
                    .doOnComplete(() -> log.info("流式执行完成"))
                    .subscribe();

            return true;

        } catch (Exception e) {
            log.error("流式执行交易时发生异常: {}", e.getMessage(), e);
            return false;
        }
    }

    // ========== 数据获取方法 ==========

    /**
     * 构造多币种市场快照，供模板和其他方法复用。
     *
     * @return 多个币种的市场数据
     */
    private List<Map<String, Object>> fetchCoinMarketData() {
        log.info("获取多币种市场数据");
        List<Map<String, Object>> snapshots = new ArrayList<>();
        for (CoinSeed seed : DEFAULT_COIN_DATA) {
            snapshots.add(buildCoinMarketEntry(seed));
        }
        return snapshots;
    }

    /**
     * 汇总 3 分钟 K 线价格，主要用于日志与检验。
     *
     * @param coinMarketData 币种市场数据
     * @return 每个币的 K 线提示
     */
    private String fetchKlineData(List<Map<String, Object>> coinMarketData) {
        log.info("获取K线数据");
        return coinMarketData.stream()
                .map(data -> data.get("coin_name") + ": [" + data.get("prices_3m") + "]")
                .collect(Collectors.joining(" | "));
    }

    /**
     * 汇总核心技术指标（EMA、MACD、RSI）。
     *
     * @param coinMarketData 币种市场数据
     * @return 指标拼接字符串
     */
    private String fetchIndicatorsData(List<Map<String, Object>> coinMarketData) {
        log.info("获取技术指标数据");
        return coinMarketData.stream()
                .map(data -> data.get("coin_name") + " EMA20=" + data.get("ema20")
                        + ", MACD=" + data.get("macd") + ", RSI7=" + data.get("rsi7"))
                .collect(Collectors.joining(" | "));
    }

    /**
     * 汇总资金费率信息。
     *
     * @param coinMarketData 币种市场数据
     * @return 资金费率拼接字符串
     */
    private String fetchFundingRate(List<Map<String, Object>> coinMarketData) {
        log.info("获取资金费率");
        return coinMarketData.stream()
                .map(data -> data.get("coin_name") + ": " + data.get("funding_rate"))
                .collect(Collectors.joining(" | "));
    }

    /**
     * 返回仓位概览，强调数量、价格、盈亏。
     *
     * @param positionSnapshot 仓位快照
     * @return 仓位说明
     */
    private String fetchPosition(PositionSnapshot positionSnapshot) {
        log.info("获取未平仓量");
        return String.format("%s 仓位 数量 %s, 入场价 %s, 当前价 %s, 未实现盈亏 %s",
                positionSnapshot.symbol(),
                decimal(positionSnapshot.quantity()),
                decimal(positionSnapshot.entryPrice()),
                decimal(positionSnapshot.currentPrice()),
                decimal(positionSnapshot.unrealizedPnl()));
    }

    /**
     * 汇总成交量信息。
     *
     * @param coinMarketData 币种市场数据
     * @return 成交量拼接字符串
     */
    private String fetchVolume(List<Map<String, Object>> coinMarketData) {
        log.info("获取成交量");
        return coinMarketData.stream()
                .map(data -> data.get("coin_name") + ": 当前 " + data.get("volume_current")
                        + ", 均值 " + data.get("volume_avg"))
                .collect(Collectors.joining(" | "));
    }

    /**
     * 账户状态概览。
     *
     * @param accountSnapshot 账户快照
     * @return 账户余额和资产描述
     */
    private String fetchAccount(AccountSnapshot accountSnapshot) {
        log.info("获取账户状态");
        return String.format("余额: $%s, 总资产: $%s",
                decimal(accountSnapshot.cashAvailable()),
                decimal(accountSnapshot.accountValue()));
    }

    /**
     * 历史表现说明。
     *
     * @return 历史分析说明
     */
    private String fetchHistoryAnalysis() {
        log.info("获取历史分析");
        return "最近7天收益率: 5.4%, 胜率: 60%, 最大回撤: 3%";
    }

    /**
     * 获取账户整体绩效指标。
     *
     * @return 绩效指标
     */
    private PerformanceMetrics fetchPerformanceMetrics() {
        return new PerformanceMetrics(12.3, 1.7);
    }

    /**
     * 获取账户资产快照。
     *
     * @return 账户快照
     */
    private AccountSnapshot fetchAccountSnapshot() {
        return new AccountSnapshot(15000, 32500);
    }

    /**
     * 获取仓位快照。
     *
     * @return 仓位快照
     */
    private PositionSnapshot fetchPositionSnapshot() {
        return new PositionSnapshot(
                "BTC",
                0.5,
                64000,
                68000,
                58000,
                2000,
                2,
                72000,
                63000,
                "close below ema20",
                0.8,
                1000,
                34000);
    }

    /**
     * 模拟距离开始交易的时间。
     *
     * @return 已经过分钟数
     */
    private int fetchMinutesElapsed() {
        return 18;
    }

    /**
     * 基于模板拼装完整的用户交易提示词。
     *
     * @param coinMarketData     币种列表
     * @param performanceMetrics 绩效指标
     * @param accountSnapshot    账户快照
     * @param positionSnapshot   仓位快照
     * @param minutesElapsed     交易已进行分钟
     * @return 模板文本
     */
    private String fetchUserTradingPrompt(List<Map<String, Object>> coinMarketData,
                                          PerformanceMetrics performanceMetrics,
                                          AccountSnapshot accountSnapshot,
                                          PositionSnapshot positionSnapshot,
                                          int minutesElapsed) {
        log.info("获取用户自定义交易策略");
        Map<String, Object> params = new HashMap<>();
        params.put("minutes_elapsed", minutesElapsed);
        params.put("coin_market_sections", buildCoinMarketSections(coinMarketData));
        params.put("return_pct", decimal(performanceMetrics.returnPct()));
        params.put("sharpe_ratio", decimal(performanceMetrics.sharpeRatio()));
        params.put("cash_available", decimal(accountSnapshot.cashAvailable()));
        params.put("account_value", decimal(accountSnapshot.accountValue()));
        params.put("coin_symbol", positionSnapshot.symbol());
        params.put("position_quantity", decimal(positionSnapshot.quantity()));
        params.put("entry_price", decimal(positionSnapshot.entryPrice()));
        params.put("current_price", decimal(positionSnapshot.currentPrice()));
        params.put("liquidation_price", decimal(positionSnapshot.liquidationPrice()));
        params.put("unrealized_pnl", decimal(positionSnapshot.unrealizedPnl()));
        params.put("leverage", decimal(positionSnapshot.leverage()));
        params.put("profit_target", decimal(positionSnapshot.profitTarget()));
        params.put("stop_loss", decimal(positionSnapshot.stopLoss()));
        params.put("invalidation_condition", positionSnapshot.invalidationCondition());
        params.put("confidence", decimal(positionSnapshot.confidence()));
        params.put("risk_usd", decimal(positionSnapshot.riskUsd()));
        params.put("notional_usd", decimal(positionSnapshot.notionalUsd()));
        return PlaceHoldersReplacingBuildingUtils.renderTemplate(User_TRADING_PROMPT_TEMPLATE_PATH,params);
    }

    /**
     * 使用 CoinMarketSections 模板构造币种段落。
     *
     * @param coinMarketData 币种数据
     * @return 拼接后的段落
     */
    private String buildCoinMarketSections(List<Map<String, Object>> coinMarketData) {
        StringBuilder sections = new StringBuilder();
        for (int i = 0; i < coinMarketData.size(); i++) {
            if (i > 0) {
                sections.append("\n\n---\n\n");
            }
            sections.append(PlaceHoldersReplacingBuildingUtils.renderTemplate(COIN_TEMPLATE_PATH,coinMarketData.get(i)));
        }
        return sections.toString();
    }

    /**
     * 将基础种子数据映射为模板参数。
     *
     * @param seed 币种基础数据
     * @return 模板参数映射
     */
    private Map<String, Object> buildCoinMarketEntry(CoinSeed seed) {
        Map<String, Object> entry = new HashMap<>();
        entry.put("coin_name", seed.name());
        entry.put("price", decimal(seed.price()));
        entry.put("ema20", decimal(seed.ema20()));
        entry.put("macd", decimal(seed.macd()));
        entry.put("rsi7", decimal(seed.rsi7()));
        entry.put("oi_latest", formatBillions(seed.oiLatest()));
        entry.put("oi_avg", formatBillions(seed.oiAvg()));
        entry.put("funding_rate", formatPercent(seed.fundingRate()));
        entry.put("prices_3m", formatSeries(seed.price() * 0.98, seed.price() * 0.99, seed.price()));
        entry.put("ema20_3m", formatSeries(seed.ema20() * 0.98, seed.ema20() * 0.99, seed.ema20()));
        entry.put("macd_3m", formatSeries(seed.macd() - 20, seed.macd() - 5, seed.macd()));
        entry.put("rsi7_3m", formatSeries(seed.rsi7() - 6, seed.rsi7() - 3, seed.rsi7()));
        entry.put("rsi14_3m", formatSeries(seed.rsi7() - 5, seed.rsi7() - 2, seed.rsi7() + 2));
        double ema20_4h = seed.price() * 0.97;
        double ema50_4h = seed.price() * 0.95;
        entry.put("ema20_4h", decimal(ema20_4h));
        entry.put("ema50_4h", decimal(ema50_4h));
        double atr3_4h = seed.price() * 0.012;
        double atr14_4h = seed.price() * 0.018;
        entry.put("atr3_4h", decimal(atr3_4h));
        entry.put("atr14_4h", decimal(atr14_4h));
        entry.put("volume_current", formatVolume(seed.volumeCurrent()));
        entry.put("volume_avg", formatVolume(seed.volumeAvg()));
        entry.put("macd_4h", formatSeries(seed.macd() - 15, seed.macd() - 5, seed.macd() + 5));
        entry.put("rsi14_4h", formatSeries(seed.rsi7() - 10, seed.rsi7() - 3, seed.rsi7() + 4));
        return entry;
    }

    /**
     * 将一组数值格式化成逗号分隔的序列字符串。
     *
     * @param values 数值
     * @return 序列字符串
     */
    private String formatSeries(double... values) {
        return Arrays.stream(values)
                .mapToObj(this::decimal)
                .collect(Collectors.joining(", "));
    }

    /**
     * 根据数值大小自动选择精度的格式化方法。
     *
     * @param value 待格式化数值
     * @return 字符串
     */
    private String decimal(double value) {
        double abs = Math.abs(value);
        if (abs >= 1) {
            return String.format("%.2f", value);
        }
        if (abs >= 0.1) {
            return String.format("%.3f", value);
        }
        return String.format("%.4f", value);
    }

    /**
     * 以 B 为单位的格式化方法。
     *
     * @param value 数值
     * @return 字符串
     */
    private String formatBillions(double value) {
        return String.format("%.2fB", value);
    }

    /**
     * 百分比格式化。
     *
     * @param value 数值
     * @return 字符串
     */
    private String formatPercent(double value) {
        return String.format("%.2f%%", value * 100);
    }

    /**
     * 用 k 表示的成交量格式化。
     *
     * @param value 数值
     * @return 字符串
     */
    private String formatVolume(double value) {
        return String.format("%.0fk", value);
    }

    /**
     * 账户整体绩效指标。
     */
    private record PerformanceMetrics(double returnPct, double sharpeRatio) {
    }

    /**
     * 账户资金和净值快照。
     */
    private record AccountSnapshot(double cashAvailable, double accountValue) {
    }

    /**
     * 当前正在跟踪的仓位详情。
     */
    private record PositionSnapshot(String symbol,
                                    double quantity,
                                    double entryPrice,
                                    double currentPrice,
                                    double liquidationPrice,
                                    double unrealizedPnl,
                                    double leverage,
                                    double profitTarget,
                                    double stopLoss,
                                    String invalidationCondition,
                                    double confidence,
                                    double riskUsd,
                                    double notionalUsd) {
    }

    /**
     * 构造模板所需的基础币种数据。
     */
    private record CoinSeed(String name,
                            double price,
                            double ema20,
                            double macd,
                            double rsi7,
                            double oiLatest,
                            double oiAvg,
                            double fundingRate,
                            double volumeCurrent,
                            double volumeAvg) {
    }
}
