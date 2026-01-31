package com.kivo.analyticsservice.messaging;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionCreatedEvent(
        Long id,
        Long userId,
        String userEmail,
        LocalDate date,
        String type,
        String category,
        BigDecimal amountBRL
) { }
