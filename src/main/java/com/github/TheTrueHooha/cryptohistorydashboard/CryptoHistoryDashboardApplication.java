package com.github.TheTrueHooha.cryptohistorydashboard;

import com.github.TheTrueHooha.cryptohistorydashboard.configurations.ConfigProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(ConfigProperties.class)
public class CryptoHistoryDashboardApplication {

	public static void main(String[] args) {
		SpringApplication.run(CryptoHistoryDashboardApplication.class, args);
	}

}
