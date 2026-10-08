package com.example.javabackend;

import com.example.javabackend.dto.UserRequest;
import com.example.javabackend.dto.UserResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void createSavesUserAndReturnsResponse() {
        UserRequest request = new UserRequest("Zarina", "zarina@example.com", 26, false);

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        UserResponse response = userService.create(request);

        assertEquals("Zarina", response.name());
        assertEquals(26, response.age());
        assertEquals("zarina@example.com", response.email());
        assertEquals(false, response.isMale());

        verify(userRepository).save(any(User.class));
    }

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
}