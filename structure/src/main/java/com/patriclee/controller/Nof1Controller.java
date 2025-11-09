package com.patriclee.controller;

import com.patriclee.domain.vo.AccountTotalsVo;
import com.patriclee.domain.vo.AnalyticsVo;
import com.patriclee.domain.vo.ConversationsVo;
import com.patriclee.domain.vo.CryptoPricesVo;
import com.patriclee.domain.vo.LeaderboardVo;
import com.patriclee.domain.vo.PositionsVo;
import com.patriclee.domain.vo.SinceInceptionVo;
import com.patriclee.domain.vo.TradesVo;
import com.patriclee.req.Response;
import com.patriclee.service.Nof1Service;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("/nof1")
@Tag(name = "NOF1-INFO", description = "NOF1 前端接口占位实现")
public class Nof1Controller {

    private final Nof1Service nof1Service;

    @GetMapping("/crypto-prices")
    @Operation(summary = "获取加密货币报价", description = "对应前端 useCryptoPrices Hook")
    public Response<CryptoPricesVo> getCryptoPrices() {
        return Response.success(nof1Service.fetchCryptoPrices());
    }

    @GetMapping("/account-totals")
    @Operation(summary = "账户汇总", description = "可通过 lastHourlyMarker 增量拉取")
    public Response<AccountTotalsVo> getAccountTotals(
            @RequestParam(value = "lastHourlyMarker", required = false) Long lastHourlyMarker) {
        return Response.success(nof1Service.fetchAccountTotals(lastHourlyMarker));
    }

    @GetMapping("/positions")
    @Operation(summary = "当前持仓列表", description = "limit 默认 1000，沿用 accountTotals 的 positions 结构")
    public Response<PositionsVo> getPositions(
            @RequestParam(value = "limit", defaultValue = "1000") int limit) {
        return Response.success(nof1Service.fetchPositions(limit));
    }

    @GetMapping("/trades")
    @Operation(summary = "成交列表", description = "返回最近成交记录")
    public Response<TradesVo> getTrades() {
        return Response.success(nof1Service.fetchTrades());
    }

    @GetMapping("/since-inception-values")
    @Operation(summary = "自成立以来表现", description = "serverTime 与数值序列用于收益曲线")
    public Response<SinceInceptionVo> getSinceInceptionValues() {
        return Response.success(nof1Service.fetchSinceInceptionValues());
    }

    @GetMapping("/leaderboard")
    @Operation(summary = "排行榜", description = "用于模型排行榜面板")
    public Response<LeaderboardVo> getLeaderboard() {
        return Response.success(nof1Service.fetchLeaderboard());
    }

    @GetMapping("/analytics")
    @Operation(summary = "多维分析", description = "提供多个 KPI 表格和聚合指标")
    public Response<AnalyticsVo> getAnalytics() {
        return Response.success(nof1Service.fetchAnalytics());
    }

    @GetMapping("/conversations")
    @Operation(summary = "模型对话", description = "返回最近的提示词和响应")
    public Response<ConversationsVo> getConversations() {
        return Response.success(nof1Service.fetchConversations());
    }
}
