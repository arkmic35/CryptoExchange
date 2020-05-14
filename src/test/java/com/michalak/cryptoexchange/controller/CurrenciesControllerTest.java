package com.michalak.cryptoexchange.controller;

import com.michalak.cryptoexchange.service.ExchangeAPI;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.michalak.cryptoexchange.TestDataProvider.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebFluxTest(CurrenciesController.class)
class CurrenciesControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private ExchangeAPI exchangeAPI;

    @Test
    @SneakyThrows
    public void fetchCurrencyRatesWithoutFiltering() {
        //given
        when(exchangeAPI.fetchRates(CURRENCY_SYMBOL))
                .thenReturn(Mono.just(CURRENCY_RATES_DTO));

        //when
        webTestClient.get()
                .uri("/currencies/{currency}", CURRENCY_SYMBOL)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                //then
                .expectStatus().isOk()
                .expectBody()
                .json(CURRENCY_RATES_RESPONSE_JSON);
    }

    @Test
    @SneakyThrows
    public void fetchCurrencyRatesWithFiltering() {
        //given
        when(exchangeAPI.fetchRates(CURRENCY_SYMBOL, List.of("usd", "eth")))
                .thenReturn(Mono.just(CURRENCY_RATES_FILTERED_DTO));

        //when
        webTestClient.get()
                .uri("/currencies/{currency}?filter[]=usd&filter[]=eth", CURRENCY_SYMBOL)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                //then
                .expectStatus().isOk()
                .expectBody()
                .json(CURRENCY_RATES_FILTERED_RESPONSE_JSON);
    }
}