package com.example.javabackend;

import com.example.javabackend.dto.UserRequest;
import com.example.javabackend.dto.UserResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserResponse> findAll() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findById(@PathVariable Long id) {
        Optional<UserResponse> user = userService.findById(id);

        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        }

        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> update(
            @PathVariable Long id,
            @Valid
            @RequestBody UserRequest request
    ) {
        Optional<UserResponse> updatedUser = userService.update(id, request);

        if (updatedUser.isPresent()) {
            return ResponseEntity.ok(updatedUser.get());
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        boolean deleted = userService.deleteById(id);

        if (deleted) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }
}