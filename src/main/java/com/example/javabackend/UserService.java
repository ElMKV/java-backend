package com.example.javabackend;

import com.example.javabackend.dto.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;


    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public Optional<AuthResponse> login(LoginRequest loginRequest) {

        Optional<User> user = userRepository.findByEmail(loginRequest.email());

        if (user.isEmpty()) {
            return Optional.empty();
        }

        if (passwordEncoder.matches(loginRequest.password(), user.get().getPasswordHash())) {
            return Optional.of(new AuthResponse(jwtService.generateToken(user.get().getEmail(), user.get().getRole())));
        }
        return Optional.empty();


    }

    public Optional<UserResponse> findByEmail(String email) {

        Optional<User> existingUser = userRepository.findByEmail(email);

        if (existingUser.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(toResponse(existingUser.get()));
    }

    public List<UserResponse> findAll() {
        return userRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public UserResponse register(RegisterRequest request) {
        User user = new User();

        user.setName(request.name());
        user.setEmail(request.email());
        user.setAge(request.age());
        user.setIsMale(request.isMale());
        user.setRole(Role.USER);
        user.setPasswordHash(passwordEncoder.encode(request.password()));

        return toResponse(userRepository.save(user));
    }

    public Optional<UserResponse> findById(Long id) {

        Optional<User> existingUser = userRepository.findById(id);

        if (existingUser.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(toResponse(existingUser.get()));


    }

    public Optional<UserResponse> update(Long id, UserRequest request) {
        Optional<User> existingUser = userRepository.findById(id);

        if (existingUser.isEmpty()) {
            return Optional.empty();
        }

        User userToUpdate = existingUser.get();
        userToUpdate.setName(request.name());
        userToUpdate.setEmail(request.email());
        userToUpdate.setAge(request.age());
        userToUpdate.setIsMale(request.isMale());
        return Optional.of(toResponse(userRepository.save(userToUpdate)));
    }

    public boolean deleteById(Long id) {
        if (!userRepository.existsById(id)) {
            return false;
        }

        userRepository.deleteById(id);
        return true;
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getAge(),
                user.getIsMale()
        );
    }

}
