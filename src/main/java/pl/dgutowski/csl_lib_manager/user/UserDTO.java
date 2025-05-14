package pl.dgutowski.csl_lib_manager.user;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class UserDTO {
    @Email
    @NotBlank(message = "E-mail address cannot be empty")
    @Size(max = 150, message = "E-mail address cannot be longer than 150 characters")
    private String email;
    @Size(max = 100, message = "Name cannot be longer than 100 characters")
    private String name;
}
