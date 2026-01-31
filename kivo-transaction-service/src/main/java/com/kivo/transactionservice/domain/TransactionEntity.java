package com.kivo.transactionservice.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "transactions")
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String userEmail;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false, length = 30)
    private String type;

    @Column(nullable = false, length = 60)
    private String category;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false, length = 5)
    private String currency;

    @Column(precision = 19, scale = 8)
    private BigDecimal exchangeRateToBRL;

    @Column(precision = 19, scale = 2)
    private BigDecimal amountBRL;

    @Column(nullable = false)
    private Instant createdAt;

    public TransactionEntity() {
    }

    public TransactionEntity(String userEmail, LocalDate date, String type, String category, BigDecimal amount, String currency) {
        this.userEmail = userEmail;
        this.date = date;
        this.type = type;
        this.category = category;
        this.amount = amount;
        this.currency = currency;
        this.createdAt = Instant.now();
    }

    public Long getId() { return id; }
    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public BigDecimal getExchangeRateToBRL() { return exchangeRateToBRL; }
    public void setExchangeRateToBRL(BigDecimal exchangeRateToBRL) { this.exchangeRateToBRL = exchangeRateToBRL; }
    public BigDecimal getAmountBRL() { return amountBRL; }
    public void setAmountBRL(BigDecimal amountBRL) { this.amountBRL = amountBRL; }
    public Instant getCreatedAt() { return createdAt; }
}
