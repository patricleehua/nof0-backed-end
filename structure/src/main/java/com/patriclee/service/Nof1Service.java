package com.patriclee.service;


import com.patriclee.domain.vo.AccountTotalsVo;
import com.patriclee.domain.vo.AnalyticsVo;
import com.patriclee.domain.vo.ConversationsVo;
import com.patriclee.domain.vo.CryptoPricesVo;
import com.patriclee.domain.vo.LeaderboardVo;
import com.patriclee.domain.vo.PositionsVo;
import com.patriclee.domain.vo.SinceInceptionVo;
import com.patriclee.domain.vo.TradesVo;
import org.springframework.stereotype.Service;

@Service
public interface Nof1Service {


    CryptoPricesVo fetchCryptoPrices();

    AccountTotalsVo fetchAccountTotals(Long lastHourlyMarker);

    PositionsVo fetchPositions(int limit);

    SinceInceptionVo fetchSinceInceptionValues();

    TradesVo fetchTrades();

    LeaderboardVo fetchLeaderboard();

    AnalyticsVo fetchAnalytics();

    ConversationsVo fetchConversations();
}
