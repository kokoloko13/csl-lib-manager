package pl.dgutowski.csl_lib_manager.auth;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.dgutowski.csl_lib_manager.auth.dto.LoginDTO;
import pl.dgutowski.csl_lib_manager.auth.dto.RegisterDTO;
import pl.dgutowski.csl_lib_manager.exception.EmailAddressInUseException;
import pl.dgutowski.csl_lib_manager.exception.EmailOrPasswordInvalidException;
import pl.dgutowski.csl_lib_manager.user.User;
import pl.dgutowski.csl_lib_manager.user.UserRepository;
import pl.dgutowski.csl_lib_manager.user.UserRole;
import pl.dgutowski.csl_lib_manager.user.dto.UserDTO;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    private final UserRepository userRepository = mock(UserRepository.class);
    private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
    private final AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
    private final JwtService jwtService = mock(JwtService.class);

    private final AuthService authService = new AuthService(userRepository, passwordEncoder, authenticationManager, jwtService);

    @Test
    void register_UserRegisteredSuccessfully() {
        RegisterDTO testRegisterDto = RegisterDTO.builder()
                .email("test@test.pl")
                .password("testPassword")
                .name("test")
                .build();

        User testSavedUser = User.builder()
                .id(UUID.randomUUID())
                .email("test@test.pl")
                .password(passwordEncoder.encode("testPassword"))
                .name("test")
                .role(UserRole.USER)
                .build();

        when(userRepository.existsByEmail(testRegisterDto.getEmail())).thenReturn(false);
        when(userRepository.save(any())).thenReturn(testSavedUser);

        UserDTO result = authService.register(testRegisterDto);

        assertEquals(
                result.getEmail(),
                "test@test.pl"
        );
    }

    @Test
    void register_UserAlreadyExist() {
        RegisterDTO testRegisterDto = RegisterDTO.builder()
                .email("test@test.pl")
                .password("testPassword")
                .name("test")
                .build();

        when(userRepository.existsByEmail(testRegisterDto.getEmail())).thenReturn(true);

        EmailAddressInUseException exception = assertThrows(EmailAddressInUseException.class, () -> authService.register(testRegisterDto));

        assertEquals("E-mail address is already in use", exception.getMessage());
    }

    @Test
    void login_UserLoggedInSuccessfully() {
        Authentication authentication = mock(Authentication.class);

        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(userRepository.findRoleByEmail(any())).thenReturn(UserRole.USER);
        when(jwtService.generateToken(any(), any())).thenReturn("testToken");

        LoginDTO testLoginDto = LoginDTO.builder()
                .email("test@test.pl")
                .password("test")
                .build();

        AuthResponse response = authService.login(testLoginDto);

        assertEquals(response.getToken(), "testToken");
    }

    @Test
    void login_WrongEmailOrPassword() {
        when(authenticationManager.authenticate(any())).thenThrow(new UsernameNotFoundException("test"));

        LoginDTO testLoginDto = LoginDTO.builder()
                .email("test@test.pl")
                .password("test")
                .build();

        EmailOrPasswordInvalidException exception = assertThrows(EmailOrPasswordInvalidException.class, () -> authService.login(testLoginDto));
        assertEquals("Invalid e-mail address or password", exception.getMessage());
    }
}
