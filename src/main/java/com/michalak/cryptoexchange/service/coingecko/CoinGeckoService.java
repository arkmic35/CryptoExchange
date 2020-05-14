package com.michalak.cryptoexchange.service.coingecko;

import com.michalak.cryptoexchange.dto.CurrenciesToBeExchangedDto;
import com.michalak.cryptoexchange.dto.CurrencyRatesDto;
import com.michalak.cryptoexchange.dto.ExchangeDataDto;
import com.michalak.cryptoexchange.exception.CurrencyNotFoundException;
import com.michalak.cryptoexchange.service.ExchangeAPI;
import com.michalak.cryptoexchange.service.coingecko.model.CurrencyDetails;
import com.michalak.cryptoexchange.service.coingecko.model.SupportedCurrency;
import com.michalak.cryptoexchange.valueobject.Rate;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Log4j2
public class CoinGeckoService implements ExchangeAPI {
    private final static MathContext FEE_ROUNDING_MATH_CONTEXT = new MathContext(8, RoundingMode.CEILING);
    private final static MathContext AMOUNT_ROUNDING_MATH_CONTEXT = new MathContext(8, RoundingMode.FLOOR);

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

    @Override
    public Mono<List<ExchangeDataDto>> exchange(CurrenciesToBeExchangedDto currenciesToBeExchanged) {
        String baseCurrencySymbol = currenciesToBeExchanged.getFrom();
        BigDecimal baseCurrencyAmount = currenciesToBeExchanged.getAmount();

        List<String> quoteCurrenciesSymbols = currenciesToBeExchanged.getTo();

        return Flux.fromIterable(quoteCurrenciesSymbols)
                .flatMap(this::fetchCurrencyId)
                .flatMap(quoteCurrencyId -> fetchFilteredCurrencyRates(quoteCurrencyId, List.of(baseCurrencySymbol)))
                .map(ratesDto ->
                        calculateExchangeDataFor(
                                ratesDto.getQuoteCurrency(),
                                baseCurrencyAmount,
                                ratesDto.getRates().iterator().next()))
                .collectList();
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

    private Mono<CurrencyRatesDto> fetchCurrencyRates(String quoteCurrencyId) {
        return WebClient.create(getCurrencyDetailsPath)
                .get()
                .uri(uriBuilder -> uriBuilder
                        .build(quoteCurrencyId))
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

    private ExchangeDataDto calculateExchangeDataFor(String quoteCurrency, BigDecimal baseCurrencyAmount, Rate rate) {
        BigDecimal exchangeRate = rate.getRate();

        BigDecimal feeInBaseCurrency = baseCurrencyAmount
                .divide(new BigDecimal(100), FEE_ROUNDING_MATH_CONTEXT);

        BigDecimal baseCurrencyToExchange = baseCurrencyAmount.subtract(feeInBaseCurrency);
        BigDecimal quoteCurrencyAmount = baseCurrencyToExchange.divide(exchangeRate, AMOUNT_ROUNDING_MATH_CONTEXT);
        BigDecimal baseCurrencyRest = baseCurrencyToExchange.subtract(quoteCurrencyAmount.multiply(exchangeRate));

        return ExchangeDataDto.of(
                rate.getBaseCurrency(),
                quoteCurrency,
                exchangeRate,
                baseCurrencyAmount,
                feeInBaseCurrency,
                baseCurrencyToExchange,
                quoteCurrencyAmount,
                baseCurrencyRest
        );
    }
}
