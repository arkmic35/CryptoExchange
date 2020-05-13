package com.michalak.cryptoexchange.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.michalak.cryptoexchange.valueobject.Rate;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CurrencyRatesDtoSerializerTest {

    private ObjectMapper objectMapper;

    private final CurrencyRatesDto SERIALIZED_SUBJECT = CurrencyRatesDto.of(
            "BTC",
            List.of(
                    Rate.of("USDT", new BigDecimal("0.3321")),
                    Rate.of("ETH", new BigDecimal("0.2911"))
            ));

    private final String EXPECTED_RESULT = "{\"source\":\"BTC\",\"rates\":{\"USDT\":0.3321,\"ETH\":0.2911}}";

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
        String result = objectMapper.writeValueAsString(SERIALIZED_SUBJECT);

        //then
        assertEquals(EXPECTED_RESULT, result);
    }
}