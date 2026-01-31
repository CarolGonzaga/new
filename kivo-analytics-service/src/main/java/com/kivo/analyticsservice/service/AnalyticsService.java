package com.kivo.analyticsservice.service;

import com.kivo.analyticsservice.domain.DailyExpenseSummary;
import com.kivo.analyticsservice.domain.DailyExpenseSummaryRepository;
import com.kivo.analyticsservice.messaging.TransactionCreatedEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class AnalyticsService {

    private final DailyExpenseSummaryRepository repo;
    private final WalletService walletService;

    public AnalyticsService(DailyExpenseSummaryRepository repo, WalletService walletService) {
        this.repo = repo;
        this.walletService = walletService;
    }

    @Transactional
    public void apply(TransactionCreatedEvent ev) {
        boolean applied = walletService.apply(ev);
        if (!applied) return;

        if (ev == null || ev.userEmail() == null || ev.date() == null || ev.category() == null || ev.amountBRL() == null) return;

        String day = ev.date().toString();
        BigDecimal add = ev.amountBRL();

        var existing = repo.findByUserEmailAndDayAndCategory(ev.userEmail(), day, ev.category());
        if (existing.isPresent()) {
            var s = existing.get();
            s.setTotalBRL(s.getTotalBRL().add(add));
        } else {
            repo.save(new DailyExpenseSummary(ev.userEmail(), day, ev.category(), add));
        }
    }
}
