package com.michalak.cryptoexchange.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.michalak.cryptoexchange.TestDataProvider.CURRENCY_RATES_DTO;
import static com.michalak.cryptoexchange.TestDataProvider.CURRENCY_RATES_RESPONSE_JSON;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CurrencyRatesDtoSerializerTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        SimpleModule module = new SimpleModule();
        module.addSerializer(CurrencyRatesDto.class, new CurrencyRatesDtoSerializer());

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(module);
    }

    @Test
    @SneakyThrows
    void canSerializeCorrectly() {
        //when
        String result = objectMapper.writeValueAsString(CURRENCY_RATES_DTO);

        //then
        assertEquals(CURRENCY_RATES_RESPONSE_JSON, result);
    }
}