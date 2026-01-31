package com.kivo.analyticsservice.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "expense_summary_daily")
public class DailyExpenseSummary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String userEmail;

    @Column(nullable = false, length = 10)
    private String day;

    @Column(nullable = false, length = 60)
    private String category;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal totalBRL;

    public DailyExpenseSummary() {
    }

    public DailyExpenseSummary(String userEmail, String day, String category, BigDecimal totalBRL) {
        this.userEmail = userEmail;
        this.day = day;
        this.category = category;
        this.totalBRL = totalBRL;
    }

    public Long getId() { return id; }
    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }
    public String getDay() { return day; }
    public void setDay(String day) { this.day = day; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public BigDecimal getTotalBRL() { return totalBRL; }
    public void setTotalBRL(BigDecimal totalBRL) { this.totalBRL = totalBRL; }
}
