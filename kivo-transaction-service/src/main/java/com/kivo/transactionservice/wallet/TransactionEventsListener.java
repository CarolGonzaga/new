package com.kivo.transactionservice.wallet;

import com.kivo.transactionservice.messaging.TransactionCreatedEvent;
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
            groupId = "kivo-transaction-service-wallet",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void onCreated(TransactionCreatedEvent ev) {
        walletService.apply(ev);
    }
}
