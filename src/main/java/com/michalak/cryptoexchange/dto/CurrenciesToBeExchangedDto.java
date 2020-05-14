package com.michalak.cryptoexchange.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
public class CurrenciesToBeExchangedDto {
    private String from;
    private List<String> to;
    private BigDecimal amount;
}
