package com.michalak.cryptoexchange.valueobject;

import lombok.NonNull;
import lombok.Value;

import java.math.BigDecimal;

@Value(staticConstructor = "of")
public class Rate {
    @NonNull String quoteCurrency;
    @NonNull BigDecimal rate;
}
