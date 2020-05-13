package com.michalak.cryptoexchange.service.coingecko;

import com.michalak.cryptoexchange.dto.CurrencyRatesDto;
import com.michalak.cryptoexchange.exception.CurrencyNotFoundException;
import com.michalak.cryptoexchange.service.ExchangeAPI;
import com.michalak.cryptoexchange.service.coingecko.model.CurrencyDetails;
import com.michalak.cryptoexchange.service.coingecko.model.SupportedCurrency;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
public class CoinGeckoService implements ExchangeAPI {

    @Value("${coingecko.url.list.currencies}")
    private String listCurrenciesPath;

    @Value("${coingecko.url.currency.data}")
    private String getCurrencyDetailsPath;

    @Override
    public Mono<CurrencyRatesDto> fetchRates(String ticker) {
        return fetchCurrencyId(ticker)
                .flatMap(this::fetchCurrencyRates)
                .map(currencyDetails -> CurrencyRatesDto.of(
                        currencyDetails.getId(),
                        currencyDetails.getRates()
                ));
    }

    @Override
    public CurrencyRatesDto fetchRates(String ticker, List<String> quoteCurrencies) {
        return null;
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
