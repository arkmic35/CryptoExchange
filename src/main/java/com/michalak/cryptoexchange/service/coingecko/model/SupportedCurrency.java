package com.michalak.cryptoexchange.service.coingecko.model;

import lombok.Data;

@Data
public class SupportedCurrency {
    String id;
    String symbol;
    String name;
}
