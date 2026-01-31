package com.kivo.transactionservice.messaging;

import com.kivo.transactionservice.wallet.WalletService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TransactionEventsListener {

    private final WalletService walletService;

    public TransactionEventsListener(WalletService walletService) {
        this.walletService = walletService;
    }

    @KafkaListener(
            topics = "${kivo.kafka.topic.transactions-created}",
            groupId = "kivo-transaction-service-wallet"
    )
    public void onTransactionCreated(TransactionCreatedEvent event) {
        walletService.apply(event);
    }
}

