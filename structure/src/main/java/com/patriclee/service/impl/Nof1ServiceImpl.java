package com.patriclee.service.impl;

import com.patriclee.domain.dto.*;
import com.patriclee.domain.vo.*;
import com.patriclee.mock.Nof1MockData;
import com.patriclee.service.Nof1Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class Nof1ServiceImpl implements Nof1Service {

    // TODO: Replace mock builders with repository/service integrations once backend data is ready.
    @Override
    public CryptoPricesVo fetchCryptoPrices() {
        Map<String, PricePointDto> prices = Nof1MockData.buildCryptoPrices();
        return new CryptoPricesVo(prices, Instant.now().toEpochMilli());
    }
    @Override
    public AccountTotalsVo fetchAccountTotals(Long lastHourlyMarker) {
        if (lastHourlyMarker != null) {
            log.debug("Account totals polled with lastHourlyMarker={}", lastHourlyMarker);
        }
        return new AccountTotalsVo(Nof1MockData.buildAccountTotals());
    }
    @Override
    public PositionsVo fetchPositions(int limit) {
        List<PositionDto> positions = Nof1MockData.buildSamplePositions().stream()
                .limit(Math.max(0, limit))
                .collect(Collectors.toList());
        return new PositionsVo(positions);
    }
    @Override
    public TradesVo fetchTrades() {
        return new TradesVo(Nof1MockData.buildTrades());
    }
    @Override
    public SinceInceptionVo fetchSinceInceptionValues() {
        return new SinceInceptionVo(Instant.now().toEpochMilli(), Nof1MockData.buildSinceInceptionRows());
    }
    @Override
    public LeaderboardVo fetchLeaderboard() {
        return new LeaderboardVo(Nof1MockData.buildLeaderboardRows());
    }
    @Override
    public AnalyticsVo fetchAnalytics() {
        return new AnalyticsVo(Nof1MockData.buildAnalyticsRows());
    }
    @Override
    public ConversationsVo fetchConversations() {
        return new ConversationsVo(Nof1MockData.buildConversations());
    }

}
