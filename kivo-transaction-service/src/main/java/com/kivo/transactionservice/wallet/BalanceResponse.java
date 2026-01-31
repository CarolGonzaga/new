package com.kivo.transactionservice.wallet;

import java.math.BigDecimal;

public record BalanceResponse(Long userId, BigDecimal balance) { }
