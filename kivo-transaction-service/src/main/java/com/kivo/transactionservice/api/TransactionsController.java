package com.kivo.transactionservice.api;

import com.kivo.transactionservice.api.dto.CreateTransactionRequest;
import com.kivo.transactionservice.api.dto.TransactionResponse;
import com.kivo.transactionservice.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionsController {

    private final TransactionService service;

    public TransactionsController(TransactionService service) {
        this.service = service;
    }

    @PostMapping
    public TransactionResponse create(@RequestBody @Valid CreateTransactionRequest req) {
        return service.create(userEmail(), userId(), req);
    }

    @GetMapping
    public List<TransactionResponse> list() {
        return service.list(userEmail());
    }

    private String userEmail() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return principal == null ? null : principal.toString();
    }

    private Long userId() {
        Object details = SecurityContextHolder.getContext().getAuthentication().getDetails();
        if (details == null) return null;
        if (details instanceof Long l) return l;
        if (details instanceof Number n) return n.longValue();
        return Long.valueOf(details.toString());
    }
}
