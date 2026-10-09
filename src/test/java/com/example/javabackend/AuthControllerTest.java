package com.example.javabackend;

import com.example.javabackend.dto.LoginRequest;
import com.example.javabackend.dto.RegisterRequest;
import com.example.javabackend.dto.UserResponse;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(AuthController.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private UserRepository userRepository;

    @MockitoBean
    private UserService userService;

    @Test
    void loginReturnsOkWhenCredentialsAreCorrect() throws Exception {

        LoginRequest request = new LoginRequest(
                "ilya@ex2ample.com",
                "12345678"
        );

        UserResponse response = new UserResponse(
                1L,
                "Ilya",
                "ilya@ex2ample.com",
                26,
                true
        );

        when(userService.login(request))
                .thenReturn(Optional.of(response));

        mockMvc.perform(post("/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {"email" : "ilya@ex2ample.com" , "password" : "12345678"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.email").exists())
                .andExpect(jsonPath("$.age").exists())
                .andExpect(jsonPath("$.password").doesNotExist())
                .andExpect(jsonPath("$.isMale").exists());
    }

    @Test
    void loginReturns401WhenCredentialsAreNotCorrect() throws Exception {

        LoginRequest request = new LoginRequest(
                "ilya@ex2ample.com",
                "87654321"
        );

        when(userService.login(request))
                .thenReturn(Optional.empty());

        mockMvc.perform(post("/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {"email" : "ilya@ex2ample.com" , "password" : "87654321"}
                                """))

                .andExpect(status().isUnauthorized());

    }

    @Test
    void registrateReturnsCreatedUsers() throws Exception {

        RegisterRequest request = new RegisterRequest(
                "Ilya",
                "ilya@ex2ample.com",
                "12345678",
                21,
                true
        );

        UserResponse response = new UserResponse(
                1L,
                "Ilya",
                "ilya@ex2ample.com",
                21,
                true
        );

        when(userService.register(request))
                .thenReturn(response);

        mockMvc.perform(post("/auth/register")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {"name":"Ilya","age":21,"isMale":true, "email" : "ilya@ex2ample.com" , "password" : "12345678"}
                                """))

                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.email").exists())
                .andExpect(jsonPath("$.age").exists())
                .andExpect(jsonPath("$.password").doesNotExist())
                .andExpect(jsonPath("$.isMale").exists());
    }

    @Test
    void registrateReturns400WhenCredentialsAreNotCorrect() throws Exception {

        mockMvc.perform(post("/auth/register")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {"name":" ","age":21,"isMale":true, "email" : "ilya@ex2ample.com" , "password" : "12345678"}
                                """))


                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.name").exists());

        verifyNoInteractions(userService);


    }

    @Test
    void registrateWithExistEmail() throws Exception {

        RegisterRequest request = new RegisterRequest(
                "Ilya",
                "ilya@ex2ample.com",
                "12345678",
                21,
                true
        );

        when(userService.register(request))
                .thenThrow(new DataIntegrityViolationException("duplicate email"));

        mockMvc.perform(post("/auth/register")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {"name":"Ilya","age":21,"isMale":true, "email" : "ilya@ex2ample.com" , "password" : "12345678"}
                                """))


                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Email уже существует"))
                .andExpect(jsonPath("$.status").value(409));


    }

}
