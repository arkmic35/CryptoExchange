package com.michalak.cryptoexchange.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.michalak.cryptoexchange.valueobject.Rate;
import lombok.Value;

import java.util.List;

@Value(staticConstructor = "of")
@JsonSerialize(using = CurrencyRatesDtoSerializer.class)
public class CurrencyRatesDto {
    String quoteCurrency;
    List<Rate> rates;
}
