package pl.dgutowski.csl_lib_manager.auth.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterDTO {
    private String email;
    private String password;
    private String name;
}
