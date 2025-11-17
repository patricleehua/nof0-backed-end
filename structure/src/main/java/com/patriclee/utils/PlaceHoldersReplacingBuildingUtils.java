package com.patriclee.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * Utility for reading the file and replacing placeholders.
 */
public final class PlaceHoldersReplacingBuildingUtils {


    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\{([^{}]+)}");

    private PlaceHoldersReplacingBuildingUtils() {
    }

    public static String renderTemplate(String templatePath, Map<String, ?> parameters) {
        Objects.requireNonNull(parameters, "parameters must not be null");
        String template = readTemplate(templatePath);
        Matcher matcher = PLACEHOLDER_PATTERN.matcher(template);
        StringBuffer resolvedTemplate = new StringBuffer();
        while (matcher.find()) {
            String key = matcher.group(1).trim();
            Object value = parameters.get(key);
            String replacement = value != null ? value.toString() : "";
            matcher.appendReplacement(resolvedTemplate, Matcher.quoteReplacement(replacement));
        }
        matcher.appendTail(resolvedTemplate);
        return resolvedTemplate.toString();
    }

    private static String readTemplate(String templatePath) {
        Resource resource = new ClassPathResource(templatePath);
        try (InputStream inputStream = resource.getInputStream()) {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException ex) {
            throw new IllegalStateException("Failed to load template: " + templatePath, ex);
        }
    }

    /**
     * Quick manual verification of the buildUserTradingPrompt method.
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        Map<String, Object> params = new HashMap<>();
        params.put("minutes_elapsed", 15);
        Map<String, Object> btc = new HashMap<>();
        btc.put("coin_name", "BTC");
        btc.put("price", 68000);
        btc.put("ema20", 67500);
        btc.put("macd", -120);
        btc.put("rsi7", 55);
        btc.put("oi_latest", "1.5B");
        btc.put("oi_avg", "1.2B");
        btc.put("funding_rate", "0.02%");
        btc.put("prices_3m", "66000, 66500, 67000");
        btc.put("ema20_3m", "66100, 66600, 67100");
        btc.put("macd_3m", "-50, -20, 10");
        btc.put("rsi7_3m", "40, 50, 55");
        btc.put("rsi14_3m", "45, 52, 57");
        btc.put("ema20_4h", 65000);
        btc.put("ema50_4h", 64000);
        btc.put("atr3_4h", 900);
        btc.put("atr14_4h", 1200);
        btc.put("volume_current", "89k");
        btc.put("volume_avg", "75k");
        btc.put("macd_4h", "-40, -20, 0");
        btc.put("rsi14_4h", "48, 53, 60");

        Map<String, Object> eth = new HashMap<>();
        eth.put("coin_name", "ETH");
        eth.put("price", 3900);
        eth.put("ema20", 3850);
        eth.put("macd", 42);
        eth.put("rsi7", 61);
        eth.put("oi_latest", "720M");
        eth.put("oi_avg", "650M");
        eth.put("funding_rate", "0.01%");
        eth.put("prices_3m", "3700, 3780, 3900");
        eth.put("ema20_3m", "3710, 3790, 3890");
        eth.put("macd_3m", "10, 25, 42");
        eth.put("rsi7_3m", "50, 55, 61");
        eth.put("rsi14_3m", "48, 54, 59");
        eth.put("ema20_4h", 3600);
        eth.put("ema50_4h", 3500);
        eth.put("atr3_4h", 120);
        eth.put("atr14_4h", 160);
        eth.put("volume_current", "45k");
        eth.put("volume_avg", "40k");
        eth.put("macd_4h", "5, 18, 30");
        eth.put("rsi14_4h", "49, 58, 64");

        String coinSections = String.join(
                "\n\n---\n\n",
                renderTemplate("template/CoinMarketSections.st",btc),
                renderTemplate("template/CoinMarketSections.st",eth));
        params.put("coin_market_sections", coinSections);
        params.put("return_pct", 12.3);
        params.put("sharpe_ratio", 1.7);
        params.put("cash_available", 15000);
        params.put("account_value", 32500);
        params.put("coin_symbol", "BTC");
        params.put("position_quantity", 0.5);
        params.put("entry_price", 64000);
        params.put("current_price", 68000);
        params.put("liquidation_price", 58000);
        params.put("unrealized_pnl", 2000);
        params.put("leverage", 2);
        params.put("profit_target", 72000);
        params.put("stop_loss", 63000);
        params.put("invalidation_condition", "close below ema20");
        params.put("confidence", 0.8);
        params.put("risk_usd", 1000);
        params.put("notional_usd", 34000);
        System.out.println(renderTemplate("template/UserTradingPromptTemplate.st", params));
    }
}
