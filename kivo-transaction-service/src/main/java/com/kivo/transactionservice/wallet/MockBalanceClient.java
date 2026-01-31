package com.kivo.transactionservice.wallet;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class MockBalanceClient {

    private final RestClient restClient;

    public MockBalanceClient(@Value("${mockapi.base-url}") String baseUrl) {
        this.restClient = RestClient.builder().baseUrl(baseUrl).build();
    }

    public BalanceResponse getBalanceByUserId(Long userId) {
        return restClient.get()
                .uri("/balances/{userId}", userId)
                .retrieve()
                .body(BalanceResponse.class);
    }
}
