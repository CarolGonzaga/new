package com.kivo.analyticsservice.api;

import com.kivo.analyticsservice.domain.DailyExpenseSummaryRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/analytics")
public class AnalyticsController {

    private final DailyExpenseSummaryRepository repo;

    public AnalyticsController(DailyExpenseSummaryRepository repo) {
        this.repo = repo;
    }

    @GetMapping("/daily")
    public Object daily(@RequestParam("day") LocalDate day) {
        return repo.findByUserEmailAndDay(userEmail(), day.toString());
    }

    @GetMapping("/daily/all")
    public Object allDaily() {
        return repo.findByUserEmail(userEmail());
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("status", "ok");
    }

    private String userEmail() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return principal == null ? null : principal.toString();
    }
}
