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
            throw new IllegalArgumentException("Email já cadastrado");
        }
        String hash = passwordEncoder.encode(req.password());
        UserEntity entity = new UserEntity(req.email(), req.name(), hash, "USER");
        UserEntity saved = userRepository.save(entity);
        return toResponse(saved);
    }

    public List<UserResponse> list() {
        return userRepository.findAll().stream().map(this::toResponse).toList();
    }

    public UserResponse me(String email) {
        return toResponse(findByEmailOrThrow(email));
    }

    @Transactional
    public UserResponse updateMe(String currentEmail, String name, String newEmail) {
        UserEntity entity = findByEmailOrThrow(currentEmail);

        if (newEmail != null && !newEmail.equalsIgnoreCase(entity.getEmail())) {
            if (userRepository.existsByEmail(newEmail)) {
                throw new IllegalArgumentException("Email já cadastrado");
            }
            entity.setEmail(newEmail);
        }

        entity.setName(name);

        return toResponse(entity);
    }

    @Transactional
    public UserResponse updateAdmin(Long id, String name, String email, Boolean active) {
        UserEntity entity = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        if (email != null && !email.equalsIgnoreCase(entity.getEmail())) {
            if (userRepository.existsByEmail(email)) {
                throw new IllegalArgumentException("Email já cadastrado");
            }
            entity.setEmail(email);
        }

        entity.setName(name);

        if (active != null) {
            entity.setActive(active);
        }

        return toResponse(entity);
    }

    @Transactional
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    public UserEntity findByEmailOrThrow(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
    }

    private UserResponse toResponse(UserEntity e) {
        return new UserResponse(
                e.getId(),
                e.getEmail(),
                e.getName(),
                e.getRole(),
                Boolean.TRUE.equals(e.getActive())
        );
    }
}
