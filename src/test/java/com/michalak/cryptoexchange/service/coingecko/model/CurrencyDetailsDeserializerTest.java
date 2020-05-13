package com.michalak.cryptoexchange.service.coingecko.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.michalak.cryptoexchange.valueobject.Rate;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CurrencyDetailsDeserializerTest {
    private ObjectMapper objectMapper;

    private final CurrencyDetails EXPECTED_RESULT = CurrencyDetails.of(
            "bitcoin",
            Arrays.asList(
                    Rate.of("eth", new BigDecimal("46.830676")),
                    Rate.of("ltc", new BigDecimal("212.581")),
                    Rate.of("usd", new BigDecimal("9061.3"))
            )
    );

    @BeforeEach
    void setUp() {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(CurrencyDetails.class, new CurrencyDetailsDeserializer());

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(module);
    }

    @Test
    @SneakyThrows
    void canSerializeCorrectly() {
        //given
        String jsonToBeDeserialized = getJsonResource();

        //when
        CurrencyDetails result = objectMapper.readValue(jsonToBeDeserialized, CurrencyDetails.class);

        //then
        assertEquals(EXPECTED_RESULT, result);
    }

    @SneakyThrows
    @SuppressWarnings("ConstantConditions")
    private String getJsonResource() {
        String resourceName = "com/michalak/cryptoexchange/service/coingecko/model/TestCurrencyDetails.json";

        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource(resourceName);

        return Files.readString(Path.of(resource.toURI()));
    }
}