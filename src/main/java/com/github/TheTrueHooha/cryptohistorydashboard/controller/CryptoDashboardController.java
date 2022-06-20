package com.github.TheTrueHooha.cryptohistorydashboard.controller;

import com.github.TheTrueHooha.cryptohistorydashboard.model.coins.CoinDetails;
import com.github.TheTrueHooha.cryptohistorydashboard.model.coins.TotalHistoryData;
import com.github.TheTrueHooha.cryptohistorydashboard.service.CryptoHistoryService;
import com.github.TheTrueHooha.cryptohistorydashboard.utils.Utility;
import io.github.dengliming.redismodule.redistimeseries.Sample;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(value = "http://localhost:3030")
@RestController
@RequestMapping("/api/v1/crypto-dashboard")
@AllArgsConstructor
@Slf4j
public class CryptoDashboardController {

    @Autowired
    private final CryptoHistoryService historyService;

    //gets a list of all the coins
    @GetMapping(path = "get-all-coins")
    public ResponseEntity<List<CoinDetails>> getAllCoins(){
        return ResponseEntity.ok().body(historyService.getAllCoinsFromRedisStore());
    }

    //gets the list of coins by time series
    @GetMapping("/{symbol}/{timePeriod}")
    public List<TotalHistoryData> getHistoryData (@PathVariable String symbol, @PathVariable String timePeriod){
        List<Sample.Value> coinHistoryData = historyService.getCoinTimeSeriesFromRedisStore(symbol, timePeriod);

        List<TotalHistoryData> totalHistoryData = coinHistoryData.stream().map(value -> new TotalHistoryData(
                        Utility.convertTimeStampToDate(value.getTimestamp()),
                        Utility.round(value.getValue(), 2))).collect(Collectors.toList());

        return totalHistoryData;
    }
}

