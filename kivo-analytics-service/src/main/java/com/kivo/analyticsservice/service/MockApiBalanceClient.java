package com.kivo.analyticsservice.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Component
public class MockApiBalanceClient {

    private final RestClient restClient;

    public MockApiBalanceClient(@Value("${mockapi.base-url}") String baseUrl) {
        this.restClient = RestClient.builder().baseUrl(baseUrl).build();
    }

    public BigDecimal buscarSaldoInicialPorEmail(String userEmail) {
        List<Map> res = restClient.get()
                .uri(uriBuilder -> uriBuilder.path("/balances").queryParam("userEmail", userEmail).build())
                .retrieve()
                .body(List.class);

        if (res == null || res.isEmpty()) return BigDecimal.ZERO;

        Object v = res.get(0).get("balance");
        if (v == null) return BigDecimal.ZERO;
        return new BigDecimal(v.toString());
    }
}
