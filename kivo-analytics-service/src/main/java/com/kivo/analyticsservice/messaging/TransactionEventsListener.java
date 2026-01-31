package com.kivo.analyticsservice.messaging;

import com.kivo.analyticsservice.service.AnalyticsService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TransactionEventsListener {

    private final AnalyticsService analyticsService;

    public TransactionEventsListener(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @KafkaListener(topics = "${kivo.kafka.topic.transactions-created}", containerFactory = "kafkaListenerContainerFactory")
    public void onCreated(TransactionCreatedEvent ev) {
        analyticsService.apply(ev);
    }
}
