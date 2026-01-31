package com.kivo.analyticsservice.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<WalletEntity, String> {
}
