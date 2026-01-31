package com.kivo.transactionservice.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Component
public class MockApiBalanceClient {

    private final RestClient restClient;

    public MockApiBalanceClient(@Value("${mockapi.base-url}") String baseUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public BigDecimal getBalanceByUserId(Long userId) {
        if (userId == null) return null;

        List<Map<String, Object>> rows = restClient.get()
                .uri("/saldo")
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});

        if (rows == null) return null;

        for (Map<String, Object> r : rows) {
            Object uid = r.get("userId");
            if (uid == null) uid = r.get("user_id");
            if (uid == null) uid = r.get("id");

            if (uid != null && uid.toString().equals(userId.toString())) {
                Object value = r.get("balance");
                if (value == null) value = r.get("saldo");
                if (value == null) return null;
                return new BigDecimal(value.toString());
            }
        }
        return null;
    }
}
