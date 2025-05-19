package pl.dgutowski.csl_lib_manager.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.dgutowski.csl_lib_manager.auth.dto.LoginDTO;
import pl.dgutowski.csl_lib_manager.auth.dto.RegisterDTO;
import pl.dgutowski.csl_lib_manager.user.dto.UserDTO;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Validated @RequestBody LoginDTO body) {
        return ResponseEntity.ok(authService.login(body));
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@Validated @RequestBody RegisterDTO body) {;
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(body));
    }
}
