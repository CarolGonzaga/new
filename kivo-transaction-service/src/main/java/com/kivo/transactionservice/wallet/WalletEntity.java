package com.kivo.transactionservice.wallet;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "wallets")
public class WalletEntity {

    @Id
    @Column(name = "user_email", nullable = false, length = 120)
    private String userEmail;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal balance;

    @Column(nullable = false)
    private Boolean initialized;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    public WalletEntity() {
    }

    public WalletEntity(String userEmail) {
        this.userEmail = userEmail;
        this.balance = BigDecimal.ZERO;
        this.initialized = false;
        this.updatedAt = Instant.now();
    }

    public String getUserEmail() { return userEmail; }
    public BigDecimal getBalance() { return balance; }
    public Boolean getInitialized() { return initialized; }
    public Instant getUpdatedAt() { return updatedAt; }

    public void initializeBalance(BigDecimal initial) {
        this.balance = initial == null ? BigDecimal.ZERO : initial;
        this.initialized = true;
        this.updatedAt = Instant.now();
    }

    public void applyDelta(BigDecimal delta) {
        if (delta == null) return;
        this.balance = this.balance.add(delta);
        this.updatedAt = Instant.now();
    }
}
