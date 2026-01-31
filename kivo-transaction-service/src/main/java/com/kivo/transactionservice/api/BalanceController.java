package com.kivo.transactionservice.api;

import com.kivo.transactionservice.service.MockApiBalanceClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/balance")
public class BalanceController {

    private final MockApiBalanceClient client;

    public BalanceController(MockApiBalanceClient client) {
        this.client = client;
    }

    @GetMapping("/{userId}")
    public BigDecimal getByUserId(@PathVariable("userId") Long userId) {
        BigDecimal balance = client.getBalanceByUserId(userId);
        return balance == null ? BigDecimal.ZERO : balance;
    }
}


