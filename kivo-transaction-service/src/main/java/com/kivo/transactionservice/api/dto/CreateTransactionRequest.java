package com.kivo.transactionservice.api.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateTransactionRequest(
        @NotNull LocalDate date,
        @NotBlank String type,
        @NotBlank String category,
        @NotNull @DecimalMin("0.01") BigDecimal amount,
        @NotBlank String currency
) { }
