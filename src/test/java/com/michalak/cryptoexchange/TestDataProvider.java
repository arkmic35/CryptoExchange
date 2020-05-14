package com.michalak.cryptoexchange;

import com.michalak.cryptoexchange.dto.CurrencyRatesDto;
import com.michalak.cryptoexchange.valueobject.Rate;

import java.math.BigDecimal;
import java.util.List;

public class TestDataProvider {

    public static final String CURRENCY_SYMBOL = "BTC";
    public static final String CURRENCY_ID = "bitcoin";

    public static final CurrencyRatesDto CURRENCY_RATES_DTO = CurrencyRatesDto.of(
            CURRENCY_ID,
            List.of(
                    Rate.of("usd", new BigDecimal("9061.3")),
                    Rate.of("eth", new BigDecimal("46.830676")),
                    Rate.of("ltc", new BigDecimal("212.581"))
            ));

    public static final String CURRENCY_RATES_RESPONSE_JSON =
            "{\"source\":\"bitcoin\",\"rates\":{\"usd\":9061.3,\"eth\":46.830676,\"ltc\":212.581}}";

    public static final CurrencyRatesDto CURRENCY_RATES_FILTERED_DTO = CurrencyRatesDto.of(
            CURRENCY_ID,
            List.of(
                    Rate.of("usd", new BigDecimal("9061.3")),
                    Rate.of("eth", new BigDecimal("46.830676"))
            ));

    public static final String CURRENCY_RATES_FILTERED_RESPONSE_JSON =
            "{\"source\":\"bitcoin\",\"rates\":{\"usd\":9061.3,\"eth\":46.830676}}";

}
