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

}