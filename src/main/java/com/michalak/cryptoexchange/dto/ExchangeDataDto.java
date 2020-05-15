package com.michalak.cryptoexchange.dto;

import lombok.Value;

import java.math.BigDecimal;

@Value(staticConstructor = "of")
public class ExchangeDataDto {
    String baseCurrency;
    String quoteCurrency;
    BigDecimal exchangeRate;

    BigDecimal baseCurrencyAmount;
    BigDecimal fee;
    BigDecimal baseCurrencyExchangedAmount;
    BigDecimal quoteCurrencyAmount;
}
