package com.github.TheTrueHooha.cryptohistorydashboard.model.coins;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CoinPriceHistory {
    private String status;
    private CoinPriceHistoryData coinPriceHistoryData;
}
