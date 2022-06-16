package com.github.TheTrueHooha.cryptohistorydashboard.configurations;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.dengliming.redismodule.redisjson.RedisJSON;
import io.github.dengliming.redismodule.redisjson.client.RedisJSONClient;
import io.github.dengliming.redismodule.redistimeseries.RedisTimeSeries;
import io.github.dengliming.redismodule.redistimeseries.client.RedisTimeSeriesClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisConfiguration {

    /*
    private static final String redisUrl = "";
    private static final String redisPassword = "";
    private static final String redisUsername = "default";
     */

    //declares the modules from the redis stack needed
    @Bean
    public Config redisConfig(){
        Dotenv dotenv = Dotenv.load();
        Config config = new Config();
        config.useSingleServer()
                .setAddress(dotenv.get("REDIS_URL"))
                .setUsername(dotenv.get("REDIS_USERNAME"))
                .setPassword(dotenv.get("REDIS_PASSWORD"));
        return config;
    }

    //redis time series client
    @Bean
    public RedisTimeSeriesClient redisTimeSeriesClient(Config config){
        return new RedisTimeSeriesClient(config);
    }

    @Bean
    //redis time series instantiates the redis time series client
    public RedisTimeSeries redisTimeSeries(RedisTimeSeriesClient redisTimeSeriesClient){
        return redisTimeSeriesClient.getRedisTimeSeries();
    }

    //redis json client
    @Bean
    public RedisJSONClient redisJSONClient(Config config){
        return new RedisJSONClient(config);
    }

    @Bean
    //redis json instantiates the redis json client
    public RedisJSON redisJSON(RedisJSONClient redisJSONClient){
        return redisJSONClient.getRedisJSON();
    }
}
