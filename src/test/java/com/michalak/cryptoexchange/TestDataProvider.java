package com.michalak.cryptoexchange;

import com.michalak.cryptoexchange.dto.CurrenciesToBeExchangedDto;
import com.michalak.cryptoexchange.dto.CurrencyRatesDto;
import com.michalak.cryptoexchange.dto.ExchangeDataDto;
import com.michalak.cryptoexchange.valueobject.Rate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

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
            "{\"target\":\"bitcoin\",\"rates\":{\"usd\":9061.3,\"eth\":46.830676,\"ltc\":212.581}}";

    public static final CurrencyRatesDto CURRENCY_RATES_FILTERED_DTO = CurrencyRatesDto.of(
            CURRENCY_ID,
            List.of(
                    Rate.of("usd", new BigDecimal("9061.3")),
                    Rate.of("eth", new BigDecimal("46.830676"))
            ));

    public static final String CURRENCY_RATES_FILTERED_RESPONSE_JSON =
            "{\"target\":\"bitcoin\",\"rates\":{\"usd\":9061.3,\"eth\":46.830676}}";

    public static final CurrenciesToBeExchangedDto CURRENCIES_TO_BE_EXCHANGED_DTO = new CurrenciesToBeExchangedDto(
            "usd",
            List.of("btc", "eth"),
            new BigDecimal(1000));

    public static final Set<ExchangeDataDto> EXCHANGE_DATA_DTOS = Set.of(
            ExchangeDataDto.of("usd", "bitcoin", new BigDecimal("9061.3"), new BigDecimal(1000), new BigDecimal(10),
                    new BigDecimal(990), new BigDecimal("0.10925584"), new BigDecimal("0.000057008")),

            ExchangeDataDto.of("usd", "ethereum", new BigDecimal("200.01"), new BigDecimal(1000), new BigDecimal(10),
                    new BigDecimal(990), new BigDecimal("4.9497525"), new BigDecimal("0.000002475"))
    );
}
