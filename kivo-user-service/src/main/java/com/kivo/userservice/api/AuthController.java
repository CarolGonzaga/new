package com.kivo.userservice.api;

import com.kivo.userservice.api.dto.LoginRequest;
import com.kivo.userservice.api.dto.TokenResponse;
import com.kivo.userservice.security.JwtService;
import com.kivo.userservice.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthController(UserService userService, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public TokenResponse login(@RequestBody @Valid LoginRequest req) {
        var user = userService.findByEmailOrThrow(req.email());

        if (!passwordEncoder.matches(req.password(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Credenciais inválidas");
        }

        String token = jwtService.generateToken(user.getId(), user.getEmail(), user.getRole());
        return new TokenResponse(token);
    }
}
