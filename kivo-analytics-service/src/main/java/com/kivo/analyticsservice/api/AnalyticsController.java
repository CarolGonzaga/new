package com.kivo.analyticsservice.api;

import com.kivo.analyticsservice.domain.DailyExpenseSummaryRepository;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/analytics")
public class AnalyticsController {

    private final DailyExpenseSummaryRepository repo;

    public AnalyticsController(DailyExpenseSummaryRepository repo) {
        this.repo = repo;
    }

    @GetMapping("/daily/{userEmail}/{day}")
    public Object daily(@PathVariable String userEmail, @PathVariable String day) {
        return repo.findByUserEmailAndDay(userEmail, day);
    }

    @GetMapping("/daily/{userEmail}")
    public Object allDaily(@PathVariable String userEmail) {
        return repo.findByUserEmail(userEmail);
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("status", "ok");
    }
}
