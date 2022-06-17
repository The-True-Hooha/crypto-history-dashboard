package com.github.TheTrueHooha.cryptohistorydashboard.service;

import com.github.TheTrueHooha.cryptohistorydashboard.model.coins.CoinData;
import com.github.TheTrueHooha.cryptohistorydashboard.utils.HttpUtils;
import io.github.dengliming.redismodule.redisjson.RedisJSON;
import io.github.dengliming.redismodule.redisjson.args.SetArgs;
import io.github.dengliming.redismodule.redisjson.utils.GsonUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
@Slf4j
public class CryptoHistoryService {

    //sets the api that is going to be called
    public static final String GET_COINS_API = "https://coinranking1.p.rapidapi.com/coins?referenceCurrencyUuid=yhjMzLPhuIDl&timePeriod=24h&tiers%5B0%5D=1&orderBy=marketCap&orderDirection=desc&limit=50&offset=0";
    public static final String REDIS_JSON_KEY = "coins";

    @Autowired
    private final RestTemplate restTemplate;

    @Autowired
    private final RedisJSON redisJSON;

    public void getAllCoins(){
        log.info("fetching the available coins");
        ResponseEntity<CoinData> coinDataResponseEntity = restTemplate.exchange(
                GET_COINS_API,
                HttpMethod.GET,
                HttpUtils.getHttpEntity(),
                CoinData.class);
        storeGetAllCoinsData(coinDataResponseEntity.getBody());
    }

    //saves the data to the redis nosql database
    private void storeGetAllCoinsData(CoinData coinData) {
        redisJSON.set(REDIS_JSON_KEY, SetArgs.Builder.create(".", GsonUtils.toJson(coinData)));
    }
}
