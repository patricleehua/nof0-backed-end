It has been {minutes_elapsed} minutes since you started trading.

Below, we are providing you with a variety of state data, price data, and predictive signals so you can discover alpha.
Below that is your current account information, value, performance, positions, etc.

⚠️ **CRITICAL: ALL OF THE PRICE OR SIGNAL DATA BELOW IS ORDERED: OLDEST → NEWEST**

**Timeframes note:** Unless stated otherwise in a section title, intraday series are provided at **3-minute intervals**.
If a coin uses a different interval, it is explicitly stated in that coin's section.

---

## CURRENT MARKET STATE FOR ALL COINS

{coin_market_sections}

---

## HERE IS YOUR ACCOUNT INFORMATION & PERFORMANCE

**Performance Metrics:**

- Current Total Return (percent): {return_pct}%
- Sharpe Ratio: {sharpe_ratio}

**Account Status:**

- Available Cash: ${cash_available}
- **Current Account Value:** ${account_value}

**Current Live Positions & Performance:**

```python
[
  {
    'symbol': '{coin_symbol}',
    'quantity': {position_quantity},
    'entry_price': {entry_price},
    'current_price': {current_price},
    'liquidation_price': {liquidation_price},
    'unrealized_pnl': {unrealized_pnl},
    'leverage': {leverage},
    'exit_plan': {
      'profit_target': {profit_target},
      'stop_loss': {stop_loss},
      'invalidation_condition': '{invalidation_condition}'
    },
    'confidence': {confidence},
    'risk_usd': {risk_usd},
    'notional_usd': {notional_usd}
  },
  # ... additional positions if any
]
```

If no open positions:

```python
[]
```

Based on the above data, provide your trading decision in the required JSON format.
