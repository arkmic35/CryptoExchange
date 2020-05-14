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
    public Mono<CurrencyRatesDto> fetchRates(String ticker) {
        log.debug("CoinGeckoService.fetchRates({})", ticker);

        return fetchCurrencyId(ticker)
                .flatMap(this::fetchCurrencyRates)
                .map(currencyDetails -> CurrencyRatesDto.of(
                        currencyDetails.getId(),
                        currencyDetails.getRates()
                ));
    }

    @Override
    public Mono<CurrencyRatesDto> fetchRates(String ticker, List<String> quoteCurrencies) {
        log.debug("CoinGeckoService.fetchRates({}, {})", ticker, quoteCurrencies);

        return fetchCurrencyId(ticker)
                .flatMap(this::fetchCurrencyRates)
                .map(currencyDetails -> CurrencyRatesDto.of(
                        currencyDetails.getId(),
                        currencyDetails.getRates()
                                .stream()
                                .filter(rate -> quoteCurrencies.contains(rate.getQuoteCurrency()))
                                .collect(Collectors.toList())
                ));
    }

    private Mono<String> fetchCurrencyId(String symbol) {
        return WebClient.create(listCurrenciesPath)
                .get()
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(SupportedCurrency.class)
                .filter(supportedCurrency -> supportedCurrency.getSymbol().equalsIgnoreCase(symbol))
                .collectList()
                .filter(list -> list.size() == 1)
                .switchIfEmpty(Mono.error(new CurrencyNotFoundException(symbol)))
                .map(list -> list.get(0))
                .map(SupportedCurrency::getId);
    }

    private Mono<CurrencyDetails> fetchCurrencyRates(String currencyId) {
        return WebClient.create(getCurrencyDetailsPath)
                .get()
                .uri(uriBuilder -> uriBuilder
                        .build(currencyId))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(CurrencyDetails.class);
    }
}
