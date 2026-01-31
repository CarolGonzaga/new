package com.kivo.transactionservice.service;

import java.math.BigDecimal;

public interface ExchangeClient {
    BigDecimal rateToBRL(String fromCurrency);
}
