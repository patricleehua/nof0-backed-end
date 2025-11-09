package com.patriclee.mock;

import com.patriclee.domain.dto.*;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public class Nof1MockData {

    public static Map<String, PricePointDto> buildCryptoPrices() {
        Instant now = Instant.now();
        return Map.of(
                "BTCUSDT", new PricePointDto("BTCUSDT", 63842.18, now.minusSeconds(5).toEpochMilli()),
                "ETHUSDT", new PricePointDto("ETHUSDT", 3110.42, now.minusSeconds(4).toEpochMilli()),
                "SOLUSDT", new PricePointDto("SOLUSDT", 178.56, now.minusSeconds(7).toEpochMilli())
        );
    }

    public static List<PositionDto> buildSamplePositions() {
        Instant now = Instant.now();
        return List.of(
                new PositionDto(
                        "entry-001",
                        "BTCUSDT",
                        62000.0,
                        63842.18,
                        0.5,
                        2.0,
                        15500.0,
                        2500.0,
                        0.82,
                        new ExitPlanDto(66000.0, 60000.0, "Break below 60k on volume"),
                        now.minusSeconds(7200).toEpochMilli(),
                        920.0,
                        null
                ),
                new PositionDto(
                        "entry-002",
                        "ETHUSDT",
                        3000.0,
                        3110.42,
                        -3.2,
                        1.5,
                        6400.0,
                        1200.0,
                        0.65,
                        new ExitPlanDto(2800.0, 3200.0, "Close if RSI > 75"),
                        now.minusSeconds(14400).toEpochMilli(),
                        -352.0,
                        -350.0
                )
        );
    }

    public static List<AccountTotalsRowDto> buildAccountTotals() {
        Instant now = Instant.now();
        return List.of(
                new AccountTotalsRowDto(
                        "model-1",
                        "acc-1",
                        now.minusSeconds(60).toEpochMilli(),
                        125000.0,
                        125000.0,
                        820.0,
                        1420.0,
                        0.012,
                        now.minusSeconds(3600).toEpochMilli(),
                        now.minusSeconds(3600).toEpochMilli(),
                        buildSamplePositions()
                ),
                new AccountTotalsRowDto(
                        "model-2",
                        "acc-2",
                        now.minusSeconds(90).toEpochMilli(),
                        86420.0,
                        86420.0,
                        -120.0,
                        230.0,
                        -0.004,
                        now.minusSeconds(7200).toEpochMilli(),
                        now.minusSeconds(7200).toEpochMilli(),
                        List.of()
                )
        );
    }

    public static List<TradeRowDto> buildTrades() {
        Instant now = Instant.now();
        return List.of(
                new TradeRowDto(
                        "trade-1",
                        "model-1",
                        "BTCUSDT",
                        "LONG",
                        61500.0,
                        63600.0,
                        0.2,
                        3.0,
                        now.minusSeconds(86400).toEpochMilli(),
                        now.minusSeconds(82800).toEpochMilli(),
                        now.minusSeconds(86400).toString(),
                        now.minusSeconds(82800).toString(),
                        420.0,
                        450.0,
                        30.0
                ),
                new TradeRowDto(
                        "trade-2",
                        "model-2",
                        "ETHUSDT",
                        "SHORT",
                        3200.0,
                        3100.0,
                        1.5,
                        1.0,
                        now.minusSeconds(43200).toEpochMilli(),
                        now.minusSeconds(39600).toEpochMilli(),
                        now.minusSeconds(43200).toString(),
                        now.minusSeconds(39600).toString(),
                        300.0,
                        320.0,
                        20.0
                )
        );
    }

    public static List<SinceInceptionRowDto> buildSinceInceptionRows() {
        Instant now = Instant.now();
        return List.of(
                new SinceInceptionRowDto("model-1", 1.42, now.minusSeconds(86400L * 120).toEpochMilli(), 420),
                new SinceInceptionRowDto("model-2", 0.98, now.minusSeconds(86400L * 90).toEpochMilli(), 320)
        );
    }

    public static List<LeaderboardRowDto> buildLeaderboardRows() {
        return List.of(
                new LeaderboardRowDto("model-1", 125000.0, 0.42, 1200, 720, 480, 2.1, 82000.0, -32000.0),
                new LeaderboardRowDto("model-2", 86420.0, 0.18, 980, 600, 380, 1.4, 61000.0, -21000.0)
        );
    }

    public static List<AnalyticsRowDto> buildAnalyticsRows() {
        return List.of(
                new AnalyticsRowDto(
                        new AnalyticsTableDto("Fee vs PnL", Map.of("fees", -1200.0, "netPnl", 8200.0)),
                        new AnalyticsTableDto("Winners vs Losers", Map.of("winners", 720d, "losers", 480d)),
                        new AnalyticsTableDto("Signals", Map.of("bullish", 0.62, "bearish", 0.38)),
                        new AnalyticsTableDto("Overview", Map.of("avgHoldMinutes", 240.0, "medianConfidence", 0.7)),
                        0.61,
                        1.3,
                        0.72,
                        0.69
                )
        );
    }

    public static List<ConversationItemDto> buildConversations() {
        Instant now = Instant.now();
        return List.of(
                new ConversationItemDto(
                        "model-1",
                        List.of(
                                new ConversationMessageDto("system", "You are an execution-focused trading model.", now.minusSeconds(300).toEpochMilli()),
                                new ConversationMessageDto("user", "Summarize BTC sentiment.", now.minusSeconds(120).toEpochMilli()),
                                new ConversationMessageDto("assistant", "Momentum remains positive; staying net long.", now.minusSeconds(60).toEpochMilli())
                        )
                )
        );
    }
}
