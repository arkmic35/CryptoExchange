package com.michalak.cryptoexchange.controller;

import com.michalak.cryptoexchange.dto.CurrenciesToBeExchangedDto;
import com.michalak.cryptoexchange.dto.CurrencyRatesDto;
import com.michalak.cryptoexchange.dto.ExchangeDataDto;
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

    @GetMapping("{quoteCurrency}")
    public Mono<CurrencyRatesDto> getCurrency(@PathVariable("quoteCurrency") String quoteCurrency,
                                              @RequestParam(value = "filter[]", required = false) List<String> baseCurrenciesFilter) {
        log.info("GET /currencies/{} baseCurrenciesFilter: {}", quoteCurrency, baseCurrenciesFilter);

        if (baseCurrenciesFilter == null) {
            return exchangeAPI.fetchRates(quoteCurrency);
        } else {
            return exchangeAPI.fetchRates(quoteCurrency, baseCurrenciesFilter);
        }
    }

    @PostMapping("exchange")
    public Mono<List<ExchangeDataDto>> exchangeCurrency(@RequestBody CurrenciesToBeExchangedDto currenciesToBeExchanged) {
        log.info("POST /currencies/exchange currenciesToBeExchanged: {}", currenciesToBeExchanged);

        return exchangeAPI.exchange(currenciesToBeExchanged);
    }
}
