package com.kivo.analyticsservice.api;

import com.kivo.analyticsservice.service.WalletQueryService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wallet")
public class WalletController {

    private final WalletQueryService walletQueryService;

    public WalletController(WalletQueryService walletQueryService) {
        this.walletQueryService = walletQueryService;
    }

    @GetMapping("/me")
    public Object me() {
        return walletQueryService.me(userEmail());
    }

    private String userEmail() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return principal == null ? null : principal.toString();
    }
}
