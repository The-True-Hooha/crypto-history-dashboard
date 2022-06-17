package com.github.TheTrueHooha.cryptohistorydashboard.model.coins;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CoinPriceHistoryData {

    private String change;
    private List<CoinPriceHistoryExchangeRate> coinPriceHistoryExchangeRates = new ArrayList<>();
}
