package com.kivo.transactionservice.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionResponse(
        Long id,
        String userEmail,
        LocalDate date,
        String type,
        String category,
        BigDecimal amount,
        String currency,
        BigDecimal exchangeRateToBRL,
        BigDecimal amountBRL
) { }
