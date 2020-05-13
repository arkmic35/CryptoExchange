package com.michalak.cryptoexchange.service.coingecko;

import com.michalak.cryptoexchange.dto.CurrencyRatesDto;
import com.michalak.cryptoexchange.exception.CurrencyNotFoundException;
import com.michalak.cryptoexchange.service.ExchangeAPI;
import com.michalak.cryptoexchange.service.coingecko.model.CurrencyDetails;
import com.michalak.cryptoexchange.service.coingecko.model.SupportedCurrency;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

public class CoinGeckoService implements ExchangeAPI {
    private static final String LIST_COINS_URL = "https://api.coingecko.com/api/v3/coins/list";
    private static final String COIN_DETAILS_URL = "https://api.coingecko.com/api/v3/coins/";

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
        return WebClient.create(LIST_COINS_URL)
                .get()
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
        return WebClient.create(COIN_DETAILS_URL + currencyId)
                .get()
                .retrieve()
                .bodyToMono(CurrencyDetails.class);
    }
}
