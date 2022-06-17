package com.github.TheTrueHooha.cryptohistorydashboard.model.coins;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CoinStats {
    private float total;
    private float referenceCurrencyRate;
    private float totalCoins;
    private float totalMarkets;
    private float totalExchanges;
    private String totalMarketCap;
    private String total24Volume;
    private float btcDominance;
    private List<CoinDetails> bestCoins = new ArrayList<>();
    private List<CoinDetails> newestCoins = new ArrayList<>();
}
