package com.michalak.cryptoexchange;

import com.michalak.cryptoexchange.service.ExchangeAPI;
import com.michalak.cryptoexchange.service.coingecko.CoinGeckoService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CryptoExchangeApplication {

    public static void main(String[] args) {
        SpringApplication.run(CryptoExchangeApplication.class, args);
    }

    @Bean
    ExchangeAPI exchangeAPI() {
        return new CoinGeckoService();
    }
}
