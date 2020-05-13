package com.michalak.cryptoexchange.controller;

import com.michalak.cryptoexchange.dto.CurrencyRatesDto;
import com.michalak.cryptoexchange.service.ExchangeAPI;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/currencies/")
@RequiredArgsConstructor
@Log4j2
public class CurrenciesController {
    private final ExchangeAPI exchangeAPI;

    @GetMapping("{currency}")
    public Mono<CurrencyRatesDto> getCurrency(@PathVariable("currency") String currency,
                                              @RequestParam(value = "filter[]", required = false) String[] rateFilters) {
        log.info("GET /currencies/{} Filter: {}", currency, rateFilters);
        return exchangeAPI.fetchRates(currency);
    }
}
