package com.kivo.transactionservice.service;

import com.kivo.transactionservice.api.dto.CreateTransactionRequest;
import com.kivo.transactionservice.api.dto.TransactionResponse;
import com.kivo.transactionservice.domain.TransactionEntity;
import com.kivo.transactionservice.domain.TransactionRepository;
import com.kivo.transactionservice.messaging.TransactionCreatedEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository repository;
    private final ExchangeClient exchangeClient;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final String topic;

    public TransactionService(TransactionRepository repository,
                              ExchangeClient exchangeClient,
                              KafkaTemplate<String, Object> kafkaTemplate,
                              @Value("${kivo.kafka.topic.transactions-created}") String topic) {
        this.repository = repository;
        this.exchangeClient = exchangeClient;
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    @Transactional
    public TransactionResponse create(String userEmail, Long userId, CreateTransactionRequest req) {
        TransactionEntity entity = new TransactionEntity(
                userEmail,
                req.date(),
                req.type(),
                req.category(),
                req.amount(),
                req.currency()
        );

        BigDecimal rate = exchangeClient.rateToBRL(req.currency());
        entity.setExchangeRateToBRL(rate);
        entity.setAmountBRL(req.amount().multiply(rate));

        TransactionEntity saved = repository.save(entity);

        kafkaTemplate.send(
                topic,
                saved.getId().toString(),
                new TransactionCreatedEvent(
                        saved.getId(),
                        userId,
                        saved.getUserEmail(),
                        saved.getDate(),
                        saved.getType(),
                        saved.getCategory(),
                        saved.getAmountBRL()
                )
        );

        return toResponse(saved);
    }

    public List<TransactionResponse> list(String userEmail) {
        return repository.findByUserEmailOrderByDateDesc(userEmail)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private TransactionResponse toResponse(TransactionEntity e) {
        return new TransactionResponse(
                e.getId(),
                e.getUserEmail(),
                e.getDate(),
                e.getType(),
                e.getCategory(),
                e.getAmount(),
                e.getCurrency(),
                e.getExchangeRateToBRL(),
                e.getAmountBRL()
        );
    }
}
