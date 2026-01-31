package com.kivo.transactionservice.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.util.Map;

@Component
public class BrasilApiExchangeClient implements ExchangeClient {

    private final RestClient restClient;

    public BrasilApiExchangeClient(@Value("${app.brasilapi.base-url}") String baseUrl) {
        this.restClient = RestClient.builder().baseUrl(baseUrl).build();
    }

    @Override
    public BigDecimal rateToBRL(String fromCurrency) {
        if (fromCurrency == null || fromCurrency.isBlank() || fromCurrency.equalsIgnoreCase("BRL")) {
            return BigDecimal.ONE;
        }

        String currency = fromCurrency.trim().toUpperCase();
        Map body = restClient.get()
                .uri("/" + currency + "-BRL")
                .retrieve()
                .body(Map.class);

        Object bid = body == null ? null : body.get("bid");
        if (bid == null) {
            return BigDecimal.ONE;
        }
        return new BigDecimal(bid.toString());
    }
}
