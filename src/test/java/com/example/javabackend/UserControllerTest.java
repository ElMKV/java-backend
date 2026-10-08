package com.example.javabackend;

import com.example.javabackend.dto.UserRequest;
import com.example.javabackend.dto.UserResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Test
    void createReturnsCreatedUser() throws Exception {
        UserRequest request = new UserRequest("Zarina", "zarina@example.com", 26, false);
        UserResponse response = new UserResponse(1L, "Zarina", "zarina@example.com", 26, false);

        when(userService.create(request)).thenReturn(response);

        mockMvc.perform(post("/users")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Zarina",
                                  "age": 26,
                                  "email": "zarina@example.com",
                                  "isMale": false
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Zarina"))
                .andExpect(jsonPath("$.email").value("zarina@example.com"))
                .andExpect(jsonPath("$.age").value(26))
                .andExpect(jsonPath("$.isMale").value(false));
    }

    @Test
    void createReturnsBadRequestWhenRequestIsInvalid() throws Exception {

        mockMvc.perform(post("/users")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "",
                                  "age": -1,
                                  "isMale": null,
                                  "email" : null
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errors.name").exists())
                .andExpect(jsonPath("$.errors.email").exists())
                .andExpect(jsonPath("$.errors.age").exists())
                .andExpect(jsonPath("$.errors.isMale").exists());
        verifyNoInteractions(userService);
    }

    @Test
    void createReturnsConflicts() throws Exception  {
        UserRequest request = new UserRequest("Zarina", "zarina@example.com", 26, false);


        when(userService.create(request))
                .thenThrow(new DataIntegrityViolationException("duplicate email"));

        mockMvc.perform(post("/users")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Zarina",
                                  "age": 26,
                                  "isMale": false,
                                  "email" : "zarina@example.com"
                                }
                                """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message").value("Email уже существует"));
    }

}