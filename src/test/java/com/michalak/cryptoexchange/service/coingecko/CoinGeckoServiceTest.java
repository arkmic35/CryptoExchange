package com.michalak.cryptoexchange.service.coingecko;

import com.michalak.cryptoexchange.exception.CurrencyNotFoundException;
import com.michalak.cryptoexchange.util.ResourceUtil;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.io.IOException;

import static com.michalak.cryptoexchange.TestDataProvider.CURRENCY_RATES_DTO;

class CoinGeckoServiceTest {
    private MockWebServer mockCoinGeckoAPI;
    private CoinGeckoService coinGeckoService;

    @BeforeEach
    void setUp() throws IOException {
        mockCoinGeckoAPI = new MockWebServer();
        mockCoinGeckoAPI.start();

        String baseUrl = String.format("http://localhost:%s", mockCoinGeckoAPI.getPort());

        coinGeckoService = new CoinGeckoService(
                baseUrl + "/api/v3/coins/list",
                baseUrl + "/api/v3/coins/{currencyId}"
        );
    }

    @Test
    void incorrectCurrencyName_throwsCurrencyNotFoundException() {
        //given
        String coinsListJson = ResourceUtil.fetchResourceContents(getClass().getPackageName(), "TestCoinGeckoCoinsList.json");

        mockCoinGeckoAPI.enqueue(
                new MockResponse()
                        .setBody(coinsListJson)
                        .addHeader("Content-type", "application/json")
        );

        //when then
        StepVerifier.create(coinGeckoService.fetchRates("XXXXX"))
                .expectError(CurrencyNotFoundException.class)
                .verify();
    }

    @Test
    void returnsCoinRates() {
        //given
        String coinsListJson = ResourceUtil.fetchResourceContents(getClass().getPackageName(), "TestCoinGeckoCoinsList.json");
        String bitcoinDetails = ResourceUtil.fetchResourceContents(getClass().getPackageName(), "TestCoinGeckoBitcoinDetails.json");

        mockCoinGeckoAPI.enqueue(
                new MockResponse()
                        .setBody(coinsListJson)
                        .addHeader("Content-type", "application/json")
        );

        mockCoinGeckoAPI.enqueue(
                new MockResponse()
                        .setBody(bitcoinDetails)
                        .addHeader("Content-type", "application/json")
        );

        //when then
        StepVerifier.create(coinGeckoService.fetchRates("btc"))
                .expectNext(CURRENCY_RATES_DTO)
                .verifyComplete();
    }

    @AfterEach
    void tearDown() throws IOException {
        if (mockCoinGeckoAPI != null) {
            mockCoinGeckoAPI.shutdown();
        }
    }
}