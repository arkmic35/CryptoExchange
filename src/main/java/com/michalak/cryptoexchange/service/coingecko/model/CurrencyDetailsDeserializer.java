package com.michalak.cryptoexchange.service.coingecko.model;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.michalak.cryptoexchange.valueobject.Rate;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CurrencyDetailsDeserializer extends StdDeserializer<CurrencyDetails> {
    public CurrencyDetailsDeserializer() {
        super((Class<?>) null);
    }

    @Override
    public CurrencyDetails deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {

        JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        String id = getId(node);
        List<Rate> rates = getRates(node);

        return CurrencyDetails.of(id, rates);
    }

    private String getId(JsonNode node) {
        return node.get("id").textValue();
    }

    private List<Rate> getRates(JsonNode node) {
        List<Rate> rates = new LinkedList<>();

        JsonNode marketData = node.get("market_data");
        JsonNode currentPrice = marketData.get("current_price");

        for (Iterator<Map.Entry<String, JsonNode>> iterator = currentPrice.fields(); iterator.hasNext(); ) {
            Map.Entry<String, JsonNode> next = iterator.next();

            rates.add(
                    Rate.of(next.getKey(), next.getValue().decimalValue())
            );
        }

        return rates;
    }
}
