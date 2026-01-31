package com.kivo.analyticsservice.domain;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "wallet_transactions_applied")
public class WalletTransactionAppliedEntity {

    @Id
    @Column(name = "transaction_id", nullable = false)
    private Long transactionId;

    @Column(name = "applied_at", nullable = false)
    private Instant appliedAt;

    public WalletTransactionAppliedEntity() {
    }

    public WalletTransactionAppliedEntity(Long transactionId) {
        this.transactionId = transactionId;
        this.appliedAt = Instant.now();
    }

    public Long getTransactionId() { return transactionId; }
    public Instant getAppliedAt() { return appliedAt; }
}
