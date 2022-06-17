package com.github.TheTrueHooha.cryptohistorydashboard;

import com.github.TheTrueHooha.cryptohistorydashboard.service.CryptoHistoryService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class StartupApplication implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private final CryptoHistoryService cryptoHistoryService;


    //calls the data from the rapid api host and parse it to the redis stack database

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        cryptoHistoryService.getAllCoins();
    }
}
