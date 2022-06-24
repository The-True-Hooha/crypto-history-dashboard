package com.github.TheTrueHooha.cryptohistorydashboard.model.coins;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Configurable;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Configurable
public class CoinDetails {
    private String uuid;
    private String symbol;
    private String name;
    private String color;
    private String iconUrl;
    private String marketCap;
    private String price;
    private Integer listedAt;
    private Integer tier;
    private String change;
    private Integer rank;
    private List<String> sparkline = new ArrayList<>();
    private Boolean lowVolume;
    private String coinrankingUrl;
    private String _24hVolume;
    private String btcPrice;
}
