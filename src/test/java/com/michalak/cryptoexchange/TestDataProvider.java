package com.michalak.cryptoexchange;

import com.michalak.cryptoexchange.dto.CurrencyRatesDto;
import com.michalak.cryptoexchange.valueobject.Rate;

import java.math.BigDecimal;
import java.util.List;

public class TestDataProvider {

    public static final String CURRENCY_SYMBOL = "Bitcoin";

    public static final CurrencyRatesDto CURRENCY_RATES_DTO = CurrencyRatesDto.of(
            "BTC",
            List.of(
                    Rate.of("USDT", new BigDecimal("0.3321")),
                    Rate.of("ETH", new BigDecimal("0.2911"))
            ));

    public static final String CURRENCY_RATES_RESPONSE_JSON =
            "{\"source\":\"BTC\",\"rates\":{\"USDT\":0.3321,\"ETH\":0.2911}}";

}
