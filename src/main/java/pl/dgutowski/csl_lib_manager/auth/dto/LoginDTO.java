package pl.dgutowski.csl_lib_manager.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Builder
public class LoginDTO {
    private String email;
    private String password;
}
