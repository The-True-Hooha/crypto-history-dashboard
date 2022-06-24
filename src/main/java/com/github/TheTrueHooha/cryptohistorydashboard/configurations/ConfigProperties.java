package com.github.TheTrueHooha.cryptohistorydashboard.configurations;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("crypto_history")
public record ConfigProperties(
        String redisUrl,
        String redisPassword,
        String redisUsername,
        String GET_COINS_API,
        String REDIS_JSON_KEY,
        String GET_COIN_PRICE_HISTORY_API,
        String COIN_HISTORY_TIME_PERIOD_PARAM

) {
}
