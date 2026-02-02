package com.kivo.analyticsservice.service;

import com.kivo.analyticsservice.domain.WalletEntity;
import com.kivo.analyticsservice.domain.WalletRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

@Service
public class WalletQueryService {

    private final WalletRepository walletRepository;
    private final MockApiBalanceClient mockApiBalanceClient;

    public WalletQueryService(WalletRepository walletRepository, MockApiBalanceClient mockApiBalanceClient) {
        this.walletRepository = walletRepository;
        this.mockApiBalanceClient = mockApiBalanceClient;
    }

    @Transactional
    public Map<String, Object> me(String userEmail) {
        WalletEntity wallet = walletRepository.findById(userEmail).orElseGet(() -> new WalletEntity(userEmail));

        if (!Boolean.TRUE.equals(wallet.getInitialized())) {
            BigDecimal initial = mockApiBalanceClient.buscarSaldoInicialPorEmail(userEmail);
            wallet.setBalance(initial == null ? BigDecimal.ZERO : initial);
            wallet.setInitialized(true);
            wallet.setUpdatedAt(Instant.now());
            walletRepository.save(wallet);
        }

        return Map.of(
                "userEmail", wallet.getUserEmail(),
                "balanceBRL", wallet.getBalance(),
                "initialized", Boolean.TRUE.equals(wallet.getInitialized()),
                "updatedAt", wallet.getUpdatedAt()
        );
    }
}
