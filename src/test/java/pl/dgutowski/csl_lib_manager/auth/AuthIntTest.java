package pl.dgutowski.csl_lib_manager.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pl.dgutowski.csl_lib_manager.AbstractTestcontainers;
import pl.dgutowski.csl_lib_manager.auth.dto.LoginDTO;
import pl.dgutowski.csl_lib_manager.auth.dto.RegisterDTO;
import pl.dgutowski.csl_lib_manager.user.UserRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class AuthIntTest extends AbstractTestcontainers {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void cleanUp() {
        userRepository.deleteAll();
    }

    @Test
    void register() throws Exception {
        RegisterDTO registerBody = RegisterDTO.builder()
                .email("test@test.pl")
                .password("Test123!")
                .name("ITTest")
                .build();

        mockMvc.perform(
                post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerBody))
                ).andExpect(status().isCreated());

    }

    @Test
    void register_UserAlreadyExists() throws Exception {
        RegisterDTO registerBody = RegisterDTO.builder()
                .email("test@test.pl")
                .password("Test123!")
                .name("ITTest")
                .build();

        mockMvc.perform(
                post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerBody))
        ).andExpect(status().isCreated());

        mockMvc.perform(
                post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerBody))
        ).andExpect(status().isConflict());
    }

    @Test
    void login() throws Exception {
        RegisterDTO registerBody = RegisterDTO.builder()
                .email("test@test.pl")
                .password("Test123!")
                .name("ITTest")
                .build();

        LoginDTO loginBody = LoginDTO.builder()
                .email("test@test.pl")
                .password("Test123!")
                .build();

        mockMvc.perform(
                post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerBody))
        ).andExpect(status().isCreated());

        mockMvc.perform(
                post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginBody))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.token").isString());
    }

    @Test
    void login_InvalidCred() throws Exception {
        RegisterDTO registerBody = RegisterDTO.builder()
                .email("test@test.pl")
                .password("Test123!")
                .name("ITTest")
                .build();

        LoginDTO loginBody = LoginDTO.builder()
                .email("invalid@test.pl")
                .password("Test123!")
                .build();

        mockMvc.perform(
                post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerBody))
        ).andExpect(status().isCreated());

        mockMvc.perform(
                        post("/api/v1/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(loginBody))
                )
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.message").isString())
                .andExpect(jsonPath("$.message").value("Invalid e-mail address or password"));
    }

}
