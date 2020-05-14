package com.michalak.cryptoexchange.controller;

import com.michalak.cryptoexchange.dto.CurrencyRatesDto;
import com.michalak.cryptoexchange.service.ExchangeAPI;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/currencies/")
@RequiredArgsConstructor
@Log4j2
public class CurrenciesController {
    private final ExchangeAPI exchangeAPI;

    @GetMapping("{currency}")
    public Mono<CurrencyRatesDto> getCurrency(@PathVariable("currency") String currency,
                                              @RequestParam(value = "filter[]", required = false) List<String> quoteCurrencies) {
        log.info("GET /currencies/{} quoteCurrencies: {}", currency, quoteCurrencies);

        if (quoteCurrencies == null) {
            return exchangeAPI.fetchRates(currency);
        } else {
            return exchangeAPI.fetchRates(currency, quoteCurrencies);
        }
    }
}
