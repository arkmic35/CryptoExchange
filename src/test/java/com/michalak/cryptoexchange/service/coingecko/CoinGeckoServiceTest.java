package com.michalak.cryptoexchange.service.coingecko;

import com.michalak.cryptoexchange.exception.CurrencyNotFoundException;
import com.michalak.cryptoexchange.util.ResourceUtil;
import lombok.SneakyThrows;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Objects;

import static com.michalak.cryptoexchange.TestDataProvider.*;

class CoinGeckoServiceTest {
    private static MockWebServer mockCoinGeckoAPI;
    private static CoinGeckoService coinGeckoService;

    @BeforeAll
    public static void beforeAll() {
        initMockWebServer();
        String baseUrl = String.format("http://localhost:%s", mockCoinGeckoAPI.getPort());

        coinGeckoService = new CoinGeckoService(
                baseUrl + "/api/v3/coins/list",
                baseUrl + "/api/v3/coins/{currencyId}"
        );
    }

    @AfterAll
    @SneakyThrows
    public static void afterAll() {
        if (mockCoinGeckoAPI != null) {
            mockCoinGeckoAPI.shutdown();
        }
    }

    @Test
    void incorrectCurrencyName_throwsCurrencyNotFoundException() {
        //when then
        StepVerifier.create(coinGeckoService.fetchRates("XXXXX"))
                .expectError(CurrencyNotFoundException.class)
                .verify();
    }

    @Test
    void returnsCoinRates() {
        //when then
        StepVerifier.create(coinGeckoService.fetchRates("btc"))
                .expectNext(CURRENCY_RATES_DTO)
                .verifyComplete();
    }

    @Test
    void returnsFilteredCoinRates() {
        //when then
        StepVerifier.create(coinGeckoService.fetchRates("btc", List.of("usd", "eth")))
                .expectNext(CURRENCY_RATES_FILTERED_DTO)
                .verifyComplete();
    }

    @Test
    void calculatesExchangeOrderInfo() {
        //when then
        StepVerifier.create(coinGeckoService.exchange(CURRENCIES_TO_BE_EXCHANGED_DTO))
                .expectNext(EXCHANGE_DATA_DTOS)
                .verifyComplete();
    }

    @SneakyThrows
    private static void initMockWebServer() {
        mockCoinGeckoAPI = new MockWebServer();
        mockCoinGeckoAPI.start();

        String packageName = CoinGeckoServiceTest.class.getPackageName();

        String coinsListJson = ResourceUtil.fetchResourceContents(packageName, "TestCoinGeckoCoinsList.json");
        String bitcoinDetails = ResourceUtil.fetchResourceContents(packageName, "TestCoinGeckoBitcoinDetails.json");
        String ethereumDetails = ResourceUtil.fetchResourceContents(packageName, "TestCoinGeckoEthereumDetails.json");

        final Dispatcher dispatcher = new Dispatcher() {
            @Override
            @NotNull
            public MockResponse dispatch(RecordedRequest request) {
                switch (Objects.requireNonNull(request.getPath())) {
                    case "/api/v3/coins/list":
                        return new MockResponse().setBody(coinsListJson).addHeader("Content-type", "application/json");
                    case "/api/v3/coins/bitcoin":
                        return new MockResponse().setBody(bitcoinDetails).addHeader("Content-type", "application/json");
                    case "/api/v3/coins/ethereum":
                        return new MockResponse().setBody(ethereumDetails).addHeader("Content-type", "application/json");
                }
                return new MockResponse().setResponseCode(404);
            }
        };
        mockCoinGeckoAPI.setDispatcher(dispatcher);
    }
}