package com.kivo.transactionservice.wallet;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AppliedTransactionRepository extends JpaRepository<AppliedTransactionEntity, Long> {
}
