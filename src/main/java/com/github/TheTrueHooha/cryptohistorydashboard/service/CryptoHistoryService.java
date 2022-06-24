package com.github.TheTrueHooha.cryptohistorydashboard.service;

import com.github.TheTrueHooha.cryptohistorydashboard.model.coins.*;
import com.github.TheTrueHooha.cryptohistorydashboard.utils.HttpUtils;
import io.github.dengliming.redismodule.redisjson.RedisJSON;
import io.github.dengliming.redismodule.redisjson.args.GetArgs;
import io.github.dengliming.redismodule.redisjson.args.SetArgs;
import io.github.dengliming.redismodule.redisjson.utils.GsonUtils;
import io.github.dengliming.redismodule.redistimeseries.DuplicatePolicy;
import io.github.dengliming.redismodule.redistimeseries.RedisTimeSeries;
import io.github.dengliming.redismodule.redistimeseries.Sample;
import io.github.dengliming.redismodule.redistimeseries.TimeSeriesOptions;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
@Slf4j
public class CryptoHistoryService {

    //sets the api that is going to be called
    public static final String GET_COINS_API = "https://coinranking1.p.rapidapi.com/coins?referenceCurrencyUuid=yhjMzLPhuIDl&timePeriod=24h&tiers%5B0%5D=1&orderBy=marketCap&orderDirection=desc&limit=50&offset=0";
    public static final String REDIS_JSON_KEY = "coin list";
    public static final String GET_COIN_PRICE_HISTORY_API = "https://coinranking1.p.rapidapi.com/coin/";
    public static final String COIN_HISTORY_TIME_PERIOD_PARAM = "/history?timePeriod=";
    public static final List<String> timePeriods = List.of("24h");

    @Autowired
    private final RestTemplate restTemplate;

    @Autowired
    private final RedisJSON redisJSON;

    @Autowired
    private final RedisTimeSeries redisTimeSeries;

    //method that gets the coin details from the API
    public void getAllCoins(){
        log.info("fetching the available coins");
        ResponseEntity<Coins> coinsResponseEntity = restTemplate.exchange(
                GET_COINS_API,
                HttpMethod.GET,
                HttpUtils.getHttpEntity(),
                Coins.class);
        storeGetAllCoinsData(coinsResponseEntity.getBody());
    }

    //method that gets the price history of the particular coin
    public void getPriceHistory(){
        log.info("getting the coin price history");
        List<CoinDetails> getAllCoins = getAllCoinsFromRedisJson();
        getAllCoins.forEach(coinDetails -> {
            timePeriods.forEach(s -> {
                getCoinDataByTimeHistory(coinDetails, s);
            });
        });

    }

    //gets the coin data by query param (time history)
    private void getCoinDataByTimeHistory(CoinDetails coinDetails, String timeHistory) {
        log.info("retrieving coin history of {} for time period {}", coinDetails.getName(), timeHistory);
        String url = GET_COIN_PRICE_HISTORY_API + coinDetails.getUuid() + COIN_HISTORY_TIME_PERIOD_PARAM + timeHistory;
        ResponseEntity<CoinPriceHistory> coinPriceHistoryResponseEntity = restTemplate.exchange(url,
                HttpMethod.GET,
                HttpUtils.getHttpEntity(),
                CoinPriceHistory.class);
        log.info("successfully retrieved coin history data {}", coinDetails.getName(), timeHistory);

        //stores the time history data to redis time series
        storeCoinHistoryDataToRedisTimeSeries(coinPriceHistoryResponseEntity.getBody(), 
                coinDetails.getSymbol(),
                timeHistory);
    }

    private void storeCoinHistoryDataToRedisTimeSeries(CoinPriceHistory coinPriceHistory, String symbol, String timeHistory) {
        log.info("saving time history data of {} at time period {}", symbol, timeHistory);
        List<CoinPriceHistoryExchangeRate> coinPriceHistoryExchangeRates = coinPriceHistory.
                getData().
                getHistory();


        //defines the way the data is extracted by time history
        coinPriceHistoryExchangeRates.stream()
                .filter(ch -> ch.getPrice() !=null && ch.getTimestamp() !=null)
                .forEach(ch -> {
                    redisTimeSeries.add(new Sample(symbol + ":" + timeHistory,
                                    Sample.Value.of(Long.valueOf(ch.getTimestamp()), Double.valueOf(ch.getPrice()))),
                            new TimeSeriesOptions().unCompressed().duplicatePolicy(DuplicatePolicy.LAST));
                });
        log.info("completed: stored coin data {} for the time history {} into the redis time series database", symbol,
                timeHistory);
    }

    //gets all the coins list from the database
    private List<CoinDetails> getAllCoinsFromRedisJson() {
        CoinData coinData = redisJSON.get(REDIS_JSON_KEY, CoinData.class,
                new GetArgs().
                        path(".data").
                        indent("\t").
                        newLine("\n").
                        space(" "));
        log.info("all coins: " + coinData);
        return coinData.getCoins();
    }

    //saves the data to the redis database
    private void storeGetAllCoinsData(Coins coins) {
        redisJSON.set(REDIS_JSON_KEY, SetArgs.Builder.create(".", GsonUtils.toJson(coins)));
    }

    public List<CoinDetails> getAllCoinsFromRedisStore() {
        return getAllCoinsFromRedisJson();
    }

    public List<Sample.Value> getCoinTimeSeriesFromRedisStore(String symbol, String timePeriod) {
        Map<String, Object> timeSeriesInfo = getTimeSeriesDataPerSymbol(symbol, timePeriod);
        Long firstTimeStamp = Long.valueOf(timeSeriesInfo.get("firstTimeStamp").toString());
        Long lastTimeStamp = Long.valueOf(timeSeriesInfo.get("lastTimeStamp").toString());

        List<Sample.Value> coinTimeSeriesData = getTimeSeriesDataPerCoin(symbol, timePeriod, firstTimeStamp, lastTimeStamp);
        return coinTimeSeriesData;
    }

    private List<Sample.Value> getTimeSeriesDataPerCoin(String symbol, String timePeriod, Long firstTimeStamp, Long lastTimeStamp) {
        String key = symbol + ":" + timePeriod;
         return redisTimeSeries.range(key, firstTimeStamp, lastTimeStamp);
    }

    private Map<String, Object> getTimeSeriesDataPerSymbol(String symbol, String timePeriod) {
        return redisTimeSeries.info(symbol + ":" + timePeriod);
    }
}
