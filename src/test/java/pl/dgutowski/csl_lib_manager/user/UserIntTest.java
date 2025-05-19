package pl.dgutowski.csl_lib_manager.user;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import pl.dgutowski.csl_lib_manager.AbstractTestcontainers;
import pl.dgutowski.csl_lib_manager.auth.JwtService;
import pl.dgutowski.csl_lib_manager.user.dto.UserDTO;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class UserIntTest extends AbstractTestcontainers {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void cleanUp() {
        userRepository.deleteAll();
    }

    @Test
    void getAllUsers_Forbidden() throws Exception {
        mockMvc.perform(get("/api/v1/user")).andExpect(status().isForbidden());
    }

    @Test
    void getAllUsers_UserRole_Fail() throws Exception {
        userRepository.save(
                User.builder()
                        .email("test@test.pl")
                        .password(encoder.encode("TestPassword!"))
                        .name("Int Test")
                        .role(UserRole.USER)
                        .build()
        );

        String token = jwtService.generateToken("test@test.pl", UserRole.USER);

        mockMvc.perform(
                get("/api/v1/user")
                        .header("Authorization", "Bearer %s".formatted(token))
        ).andExpect(status().isForbidden());
    }

    @Test
    void getAllUsers_AdminRole_Success() throws Exception {
        userRepository.save(
                User.builder()
                        .email("test@test.pl")
                        .password(encoder.encode("TestPassword!"))
                        .name("Int Test")
                        .role(UserRole.ADMIN)
                        .build()
        );

        String token = jwtService.generateToken("test@test.pl", UserRole.ADMIN);

        MvcResult result = mockMvc.perform(
                get("/api/v1/user")
                        .header("Authorization", "Bearer %s".formatted(token))
        ).andExpect(status().isOk()).andReturn();

        List<UserDTO> userList = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>(){});

        assertFalse(userList.isEmpty());
        assertEquals(userList.size(), 1);
    }
}
