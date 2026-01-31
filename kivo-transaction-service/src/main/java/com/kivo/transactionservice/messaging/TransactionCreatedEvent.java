package com.kivo.transactionservice.messaging;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionCreatedEvent(
        Long transactionId,
        Long userId,
        String userEmail,
        LocalDate date,
        String type,
        String category,
        BigDecimal amountBRL
) { }
