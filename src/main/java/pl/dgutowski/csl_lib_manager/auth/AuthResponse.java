package pl.dgutowski.csl_lib_manager.auth;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AuthResponse {
    private String token;
}
