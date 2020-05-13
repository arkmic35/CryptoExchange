package com.michalak.cryptoexchange.service;

import com.michalak.cryptoexchange.dto.CurrencyRatesDto;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ExchangeAPI {

    Mono<CurrencyRatesDto> fetchRates(String ticker);

    CurrencyRatesDto fetchRates(String ticker, List<String> quoteCurrencies);
}
