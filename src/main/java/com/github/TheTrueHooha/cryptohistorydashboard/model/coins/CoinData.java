package com.github.TheTrueHooha.cryptohistorydashboard.model.coins;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CoinData {

    private CoinStats stats;
    private List<CoinDetails> coins = new ArrayList<>();
}
