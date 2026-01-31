package com.kivo.analyticsservice.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface DailyExpenseSummaryRepository extends JpaRepository<DailyExpenseSummary, Long> {
    Optional<DailyExpenseSummary> findByUserEmailAndDayAndCategory(String userEmail, String day, String category);
    List<DailyExpenseSummary> findByUserEmailAndDay(String userEmail, String day);
    List<DailyExpenseSummary> findByUserEmail(String userEmail);
}
