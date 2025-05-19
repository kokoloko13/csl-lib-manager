package pl.dgutowski.csl_lib_manager.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.dgutowski.csl_lib_manager.auth.dto.LoginDTO;
import pl.dgutowski.csl_lib_manager.auth.dto.RegisterDTO;
import pl.dgutowski.csl_lib_manager.exception.ApiError;
import pl.dgutowski.csl_lib_manager.exception.EmailAddressInUseException;
import pl.dgutowski.csl_lib_manager.exception.EmailOrPasswordInvalidException;
import pl.dgutowski.csl_lib_manager.exception.ExceptionMessage;
import pl.dgutowski.csl_lib_manager.user.User;
import pl.dgutowski.csl_lib_manager.user.UserRepository;
import pl.dgutowski.csl_lib_manager.user.UserRole;
import pl.dgutowski.csl_lib_manager.user.dto.UserDTO;
import pl.dgutowski.csl_lib_manager.utils.DtoUtils;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public UserDTO register(RegisterDTO body) throws EmailAddressInUseException {
        if(userRepository.existsByEmail(body.getEmail())) {
            throw new EmailAddressInUseException(ExceptionMessage.EMAIL_ADDRESS_IN_USE);
        }

            User savedUser = userRepository.save(
                    User.builder()
                            .email(body.getEmail())
                            .password(passwordEncoder.encode(body.getPassword()))
                            .name(!body.getName().isBlank() ? body.getName() : null)
                            .role(UserRole.USER)
                            .build()
            );

            return DtoUtils.userToDto(savedUser);
    }

    public AuthResponse login(LoginDTO body) {
        AuthResponse authResponse = null;

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(body.getEmail(), body.getPassword())
            );
            if(authentication.isAuthenticated()) {
                UserRole userRole = userRepository.findRoleByEmail(body.getEmail());

                authResponse = AuthResponse.builder()
                        .token(jwtService.generateToken(body.getEmail(), userRole))
                        .build();
            }

        } catch (UsernameNotFoundException | BadCredentialsException e) {
            throw new EmailOrPasswordInvalidException(ExceptionMessage.EMAIL_OR_PASSWORD_INVALID);
        }

        return authResponse;
    }
}
