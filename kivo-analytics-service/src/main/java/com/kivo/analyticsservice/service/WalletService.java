package com.kivo.analyticsservice.service;

import com.kivo.analyticsservice.domain.WalletEntity;
import com.kivo.analyticsservice.domain.WalletRepository;
import com.kivo.analyticsservice.domain.WalletTransactionAppliedEntity;
import com.kivo.analyticsservice.domain.WalletTransactionAppliedRepository;
import com.kivo.analyticsservice.messaging.TransactionCreatedEvent;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;

@Service
public class WalletService {

    private final WalletRepository walletRepository;
    private final WalletTransactionAppliedRepository appliedRepository;
    private final MockApiBalanceClient mockApiBalanceClient;

    public WalletService(WalletRepository walletRepository,
                         WalletTransactionAppliedRepository appliedRepository,
                         MockApiBalanceClient mockApiBalanceClient) {
        this.walletRepository = walletRepository;
        this.appliedRepository = appliedRepository;
        this.mockApiBalanceClient = mockApiBalanceClient;
    }

    @Transactional
    public boolean apply(TransactionCreatedEvent ev) {
        if (ev == null || ev.id() == null || ev.userEmail() == null || ev.amountBRL() == null || ev.type() == null) return false;

        try {
            appliedRepository.save(new WalletTransactionAppliedEntity(ev.id()));
        } catch (DataIntegrityViolationException e) {
            return false;
        }

        WalletEntity wallet = walletRepository.findById(ev.userEmail()).orElseGet(() -> new WalletEntity(ev.userEmail()));

        if (!Boolean.TRUE.equals(wallet.getInitialized())) {
            BigDecimal initial = mockApiBalanceClient.buscarSaldoInicialPorEmail(ev.userEmail());
            wallet.setBalance(initial == null ? BigDecimal.ZERO : initial);
            wallet.setInitialized(true);
        }

        BigDecimal delta = signedAmount(ev.type(), ev.amountBRL());
        wallet.setBalance(wallet.getBalance().add(delta));
        wallet.setUpdatedAt(Instant.now());

        walletRepository.save(wallet);
        return true;
    }

    private BigDecimal signedAmount(String type, BigDecimal amountBRL) {
        String t = type.trim().toUpperCase();
        if (t.equals("DEPOSITO") || t.equals("DEPÓSITO")) return amountBRL;
        return amountBRL.negate();
    }
}
