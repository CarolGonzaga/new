package com.kivo.analyticsservice.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletTransactionAppliedRepository extends JpaRepository<WalletTransactionAppliedEntity, Long> {
}
