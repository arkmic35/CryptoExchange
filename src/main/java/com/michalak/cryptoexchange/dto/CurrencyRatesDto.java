package com.michalak.cryptoexchange.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Value;

import java.math.BigDecimal;
import java.util.List;

@Value(staticConstructor = "of")
@JsonSerialize(using = CurrencyRatesDtoSerializer.class)
public class CurrencyRatesDto {
    String baseCurrency;
    List<Rate> rates;

    @Value(staticConstructor = "of")
    public static class Rate {
        String quoteCurrency;
        BigDecimal rate;
    }
}
