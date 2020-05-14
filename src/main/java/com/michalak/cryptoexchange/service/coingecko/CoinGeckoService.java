package com.michalak.cryptoexchange.service.coingecko;

import com.michalak.cryptoexchange.dto.CurrencyRatesDto;
import com.michalak.cryptoexchange.exception.CurrencyNotFoundException;
import com.michalak.cryptoexchange.service.ExchangeAPI;
import com.michalak.cryptoexchange.service.coingecko.model.CurrencyDetails;
import com.michalak.cryptoexchange.service.coingecko.model.SupportedCurrency;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Log4j2
public class CoinGeckoService implements ExchangeAPI {
    @Value("${coingecko.url.list.currencies}")
    private String listCurrenciesPath;

    @Value("${coingecko.url.currency.data}")
    private String getCurrencyDetailsPath;

    @Override
    public Mono<CurrencyRatesDto> fetchRates(String quoteCurrency) {
        log.debug("CoinGeckoService.fetchRates({})", quoteCurrency);

        return fetchCurrencyId(quoteCurrency)
                .flatMap(this::fetchCurrencyRates);
    }

    @Override
    public Mono<CurrencyRatesDto> fetchRates(String quoteCurrency, List<String> baseCurrenciesFilter) {
        log.debug("CoinGeckoService.fetchRates({}, {})", quoteCurrency, baseCurrenciesFilter);

        return fetchCurrencyId(quoteCurrency)
                .flatMap(quoteCurrencyId -> fetchFilteredCurrencyRates(quoteCurrencyId, baseCurrenciesFilter));
    }

    private Mono<String> fetchCurrencyId(String currencySymbol) {
        return WebClient.create(listCurrenciesPath)
                .get()
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(SupportedCurrency.class)
                .filter(supportedCurrency -> supportedCurrency.getSymbol().equalsIgnoreCase(currencySymbol))
                .collectList()
                .filter(list -> list.size() == 1)
                .switchIfEmpty(Mono.error(new CurrencyNotFoundException(currencySymbol)))
                .map(list -> list.get(0))
                .map(SupportedCurrency::getId);
    }

    private Mono<CurrencyRatesDto> fetchCurrencyRates(String quoteCurrency) {
        return WebClient.create(getCurrencyDetailsPath)
                .get()
                .uri(uriBuilder -> uriBuilder
                        .build(quoteCurrency))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(CurrencyDetails.class)
                .map(currencyDetails -> CurrencyRatesDto.of(
                        currencyDetails.getId(),
                        currencyDetails.getRates()
                ));
    }

    private Mono<CurrencyRatesDto> fetchFilteredCurrencyRates(String quoteCurrencyId, List<String> baseCurrenciesFilter) {
        return fetchCurrencyRates(quoteCurrencyId)
                .map(currencyRatesDto -> CurrencyRatesDto.of(
                        currencyRatesDto.getQuoteCurrency(),
                        currencyRatesDto.getRates()
                                .stream()
                                .filter(rate -> baseCurrenciesFilter.contains(rate.getBaseCurrency()))
                                .collect(Collectors.toList())));
    }


}
