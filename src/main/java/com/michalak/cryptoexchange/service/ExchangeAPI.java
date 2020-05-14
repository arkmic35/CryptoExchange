package com.michalak.cryptoexchange.service;

import com.michalak.cryptoexchange.dto.CurrenciesToBeExchangedDto;
import com.michalak.cryptoexchange.dto.CurrencyRatesDto;
import com.michalak.cryptoexchange.dto.ExchangeDataDto;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;

public interface ExchangeAPI {

    Mono<CurrencyRatesDto> fetchRates(String quoteCurrency);

    Mono<CurrencyRatesDto> fetchRates(String quoteCurrency, List<String> baseCurrenciesFilter);

    Mono<Set<ExchangeDataDto>> exchange(CurrenciesToBeExchangedDto currenciesToBeExchanged);
}
