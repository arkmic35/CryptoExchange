package com.michalak.cryptoexchange.exception;

public class CurrencyNotFoundException extends RuntimeException {
    private static final String MESSAGE = "Currency with symbol %s was not found";

    public CurrencyNotFoundException(String currencyName) {
        super(String.format(MESSAGE, currencyName));
    }
}
