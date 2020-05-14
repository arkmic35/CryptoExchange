package com.michalak.cryptoexchange.dto;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.michalak.cryptoexchange.valueobject.Rate;

import java.io.IOException;

public class CurrencyRatesDtoSerializer extends StdSerializer<CurrencyRatesDto> {

    public CurrencyRatesDtoSerializer() {
        super((Class<CurrencyRatesDto>) null);
    }

    @Override
    public void serialize(CurrencyRatesDto value, JsonGenerator gen, SerializerProvider provider) throws IOException {

        gen.writeStartObject();
        gen.writeStringField("target", value.getQuoteCurrency());

        gen.writeObjectFieldStart("rates");

        for (Rate rate : value.getRates()) {
            gen.writeNumberField(rate.getBaseCurrency(), rate.getRate());
        }
        gen.writeEndObject();

        gen.writeEndObject();
    }

}
