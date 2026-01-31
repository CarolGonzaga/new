package com.kivo.userservice.api;

import com.kivo.userservice.api.dto.CreateUserRequest;
import com.kivo.userservice.api.dto.UserResponse;
import com.kivo.userservice.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UsersController {

    private final UserService userService;

    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public UserResponse create(@RequestBody @Valid CreateUserRequest req) {
        return userService.create(req);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> list() {
        return userService.list();
    }

    @GetMapping("/me")
    public UserResponse me() {
        return userService.me(currentEmail());
    }

    public record UpdateMeRequest(
            @NotBlank @Size(min = 2, max = 120) String name,
            @Email @Size(max = 120) String email
    ) {}

    @PutMapping("/me")
    public UserResponse updateMe(@RequestBody @Valid UpdateMeRequest req) {
        return userService.updateMe(currentEmail(), req.name(), req.email());
    }

    public record UpdateUserRequest(
            @NotBlank @Size(min = 2, max = 120) String name,
            @Email @Size(max = 120) String email,
            Boolean active
    ) {}

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse update(@PathVariable("id") Long id, @RequestBody @Valid UpdateUserRequest req) {
        return userService.updateAdmin(id, req.name(), req.email(), req.active());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable("id") Long id) {
        userService.delete(id);
    }

    private String currentEmail() {
        var auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        Object principal = auth == null ? null : auth.getPrincipal();
        return principal == null ? null : principal.toString();
    }
}
