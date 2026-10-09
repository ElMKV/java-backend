package com.example.javabackend;

import com.example.javabackend.dto.LoginRequest;
import com.example.javabackend.dto.UserRequest;
import com.example.javabackend.dto.UserResponse;
import org.apache.juli.logging.Log;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;


    @InjectMocks
    private UserService userService;


    @Test
    void findByIdReturnsUserResponseWhenUserExists() {

        final User fakeUser = new User();
        fakeUser.setIsMale(false);
        fakeUser.setAge(12);
        fakeUser.setEmail("test@example.com");
        fakeUser.setName("Name test");

        when(userRepository.findById(1L)).thenReturn(Optional.of(fakeUser));

        Optional<UserResponse> response = userService.findById(1L);

        assertFalse(response.isEmpty());

        assertEquals(fakeUser.getName(), response.get().name());
        assertEquals(fakeUser.getEmail(), response.get().email());
        assertEquals(fakeUser.getAge(), response.get().age());
        assertEquals(fakeUser.getIsMale(), response.get().isMale());

        verify(userRepository).findById(1L);
    }

    @Test
    void loginReturnsUserResponseWhenUserExists() {
        final LoginRequest fakeRequest = new LoginRequest("ilya@Mail.com", "12345678");

        final User fakeUser = new User();
        fakeUser.setIsMale(false);
        fakeUser.setAge(12);
        fakeUser.setEmail("test@example.com");
        fakeUser.setName("Name test");
        fakeUser.setPasswordHash("hashed-password");

        when(userRepository.findByEmail(fakeRequest.email())).thenReturn(Optional.of(fakeUser));
        when(passwordEncoder.matches(
                fakeRequest.password(),
                fakeUser.getPasswordHash()
        )).thenReturn(true);
        
        Optional<UserResponse> response = userService.login(fakeRequest);

        assertFalse(response.isEmpty());

        assertEquals(fakeUser.getName(), response.get().name());
        assertEquals(fakeUser.getEmail(), response.get().email());
        assertEquals(fakeUser.getAge(), response.get().age());
        assertEquals(fakeUser.getIsMale(), response.get().isMale());


    }
}