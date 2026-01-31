package com.kivo.transactionservice.wallet;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "wallet_transactions_applied")
public class AppliedTransactionEntity {

    @Id
    @Column(name = "transaction_id", nullable = false)
    private Long transactionId;

    @Column(name = "applied_at", nullable = false)
    private Instant appliedAt;

    public AppliedTransactionEntity() {
    }

    public AppliedTransactionEntity(Long transactionId) {
        this.transactionId = transactionId;
        this.appliedAt = Instant.now();
    }

    public Long getTransactionId() { return transactionId; }
    public Instant getAppliedAt() { return appliedAt; }
}
