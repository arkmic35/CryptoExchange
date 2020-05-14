package com.michalak.cryptoexchange.service;

import com.michalak.cryptoexchange.dto.CurrencyRatesDto;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ExchangeAPI {

    Mono<CurrencyRatesDto> fetchRates(String quoteCurrency);

    Mono<CurrencyRatesDto> fetchRates(String quoteCurrency, List<String> baseCurrenciesFilter);
}
