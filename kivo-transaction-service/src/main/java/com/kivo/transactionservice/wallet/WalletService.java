package com.kivo.transactionservice.wallet;

import com.kivo.transactionservice.messaging.TransactionCreatedEvent;
import com.kivo.transactionservice.service.MockApiBalanceClient;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class WalletService {

    private final WalletRepository walletRepository;
    private final AppliedTransactionRepository appliedTransactionRepository;
    private final MockApiBalanceClient mockApiBalanceClient;

    public WalletService(WalletRepository walletRepository,
                         AppliedTransactionRepository appliedTransactionRepository,
                         MockApiBalanceClient mockApiBalanceClient) {
        this.walletRepository = walletRepository;
        this.appliedTransactionRepository = appliedTransactionRepository;
        this.mockApiBalanceClient = mockApiBalanceClient;
    }

    @Transactional
    public void apply(TransactionCreatedEvent event) {
        Long transactionId = event.transactionId();

        try {
            appliedTransactionRepository.save(new AppliedTransactionEntity(transactionId));
        } catch (DataIntegrityViolationException e) {
            return;
        }

        String userEmail = event.userEmail();

        WalletEntity wallet = walletRepository.findById(userEmail)
                .orElseGet(() -> walletRepository.save(new WalletEntity(userEmail)));

        if (!Boolean.TRUE.equals(wallet.getInitialized())) {
            BigDecimal initial = mockApiBalanceClient.getBalanceByUserId(event.userId());
            wallet.initializeBalance(initial == null ? BigDecimal.ZERO : initial);
        }

        BigDecimal delta = calculateDelta(event.type(), event.amountBRL());
        wallet.applyDelta(delta);

        walletRepository.save(wallet);
    }

    private BigDecimal calculateDelta(String type, BigDecimal amount) {
        String t = type == null ? "" : type.trim().toUpperCase();

        return switch (t) {
            case "DEPOSITO", "DEPÓSITO" -> amount;
            case "RETIRADA", "COMPRA", "TRANSFERENCIA", "TRANSFERÊNCIA" -> amount.negate();
            default -> amount;
        };
    }
}
