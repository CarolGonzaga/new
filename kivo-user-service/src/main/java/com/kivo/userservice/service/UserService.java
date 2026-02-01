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

    private final UserRepository repo;
    private final PasswordEncoder encoder;

    public UserService(UserRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    @Transactional
    public UserResponse create(CreateUserRequest req) {
        if (repo.existsByEmail(req.email())) {
            throw new IllegalArgumentException("Email já cadastrado");
        }

        UserEntity user = new UserEntity();
        user.setName(req.name());
        user.setEmail(req.email());
        user.setPasswordHash(encoder.encode(req.password()));
        user.setRole("USER");
        user.setActive(true);

        repo.save(user);
        return toResponse(user);
    }

    public List<UserResponse> list() {
        return repo.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public UserResponse me(String email) {
        UserEntity user = repo.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
        return toResponse(user);
    }

    @Transactional
    public UserResponse updateMe(String email, String name, String newEmail) {
        UserEntity user = repo.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        if (!Boolean.TRUE.equals(user.getActive())) {
            throw new IllegalArgumentException("Usuário inativo");
        }

        if (!user.getEmail().equalsIgnoreCase(newEmail) && repo.existsByEmail(newEmail)) {
            throw new IllegalArgumentException("Email já cadastrado");
        }

        user.setName(name);
        user.setEmail(newEmail);

        return toResponse(user);
    }

    @Transactional
    public UserResponse updateAdmin(Long id, String name, String newEmail) {
        UserEntity user = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        if (!Boolean.TRUE.equals(user.getActive())) {
            throw new IllegalArgumentException("Usuário inativo");
        }

        if (!user.getEmail().equalsIgnoreCase(newEmail) && repo.existsByEmail(newEmail)) {
            throw new IllegalArgumentException("Email já cadastrado");
        }

        user.setName(name);
        user.setEmail(newEmail);

        return toResponse(user);
    }

    @Transactional
    public void deleteMe(String email) {
        UserEntity user = repo.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        if (!Boolean.TRUE.equals(user.getActive())) {
            return;
        }

        user.setActive(false);
    }

    @Transactional
    public void delete(Long id) {
        UserEntity user = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        if (!Boolean.TRUE.equals(user.getActive())) {
            return;
        }

        user.setActive(false);
    }

    private UserResponse toResponse(UserEntity user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getRole(),
                user.getActive()
        );
    }
}
