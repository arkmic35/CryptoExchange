package com.michalak.cryptoexchange.service.coingecko.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.michalak.cryptoexchange.valueobject.Rate;
import lombok.Value;

import java.util.List;

@Value(staticConstructor = "of")
@JsonDeserialize(using = CurrencyDetailsDeserializer.class)
public class CurrencyDetails {
    String id;
    List<Rate> rates;
}
