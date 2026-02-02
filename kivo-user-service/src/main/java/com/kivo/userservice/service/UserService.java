package com.kivo.userservice.service;

import com.kivo.userservice.api.dto.CreateUserRequest;
import com.kivo.userservice.api.dto.UserResponse;
import com.kivo.userservice.domain.UserEntity;
import com.kivo.userservice.domain.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserResponse create(CreateUserRequest req) {
        if (userRepository.existsByEmail(req.email())) {
            throw new BusinessRuleException("Email já cadastrado");
        }

        var user = new UserEntity();
        user.setEmail(req.email());
        user.setName(req.name());
        user.setPasswordHash(passwordEncoder.encode(req.password()));
        user.setRole("USER");
        user.setActive(true);

        userRepository.save(user);
        return new UserResponse(user);
    }

    public List<UserResponse> list() {
        return userRepository.findAll()
                .stream()
                .map(UserResponse::new)
                .toList();
    }

    public UserResponse me(Long userId) {
        if (userId == null) throw new BusinessRuleException("Token inválido");
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessRuleException("Usuário não encontrado"));
        return new UserResponse(user);
    }

    @Transactional
    public UserResponse updateMe(Long userId, String name, String email) {
        if (userId == null) throw new BusinessRuleException("Token inválido");

        var user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessRuleException("Usuário não encontrado"));

        if (email != null && !email.equalsIgnoreCase(user.getEmail())) {
            if (userRepository.existsByEmail(email)) {
                throw new BusinessRuleException("Email já cadastrado");
            }
            user.setEmail(email);
        }

        user.setName(name);
        userRepository.save(user);

        return new UserResponse(user);
    }

    @Transactional
    public void deleteMe(Long userId) {
        if (userId == null) throw new BusinessRuleException("Token inválido");

        var user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessRuleException("Usuário não encontrado"));

        user.setActive(false);
        userRepository.save(user);
    }

    @Transactional
    public UserResponse updateAdmin(Long id, String name, String email) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new BusinessRuleException("Usuário não encontrado"));

        if (email != null && !email.equalsIgnoreCase(user.getEmail())) {
            if (userRepository.existsByEmail(email)) {
                throw new BusinessRuleException("Email já cadastrado");
            }
            user.setEmail(email);
        }

        user.setName(name);
        userRepository.save(user);

        return new UserResponse(user);
    }

    @Transactional
    public void delete(Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new BusinessRuleException("Usuário não encontrado"));

        user.setActive(false);
        userRepository.save(user);
    }
}
