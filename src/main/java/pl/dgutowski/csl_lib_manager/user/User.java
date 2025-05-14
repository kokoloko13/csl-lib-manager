package pl.dgutowski.csl_lib_manager.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Email
    @Column(unique = true)
    @NotBlank(message = "E-mail address cannot be empty")
    @Size(max = 150, message = "E-mail address cannot be longer than 150 characters")
    private String email;
    @NotBlank(message = "E-mail address cannot be empty")
    @Size(min = 8, message = "Password cannot be shorter than 8 characters")
    private String password;
    @Size(max = 100, message = "Name cannot be longer than 100 characters")
    private String name;

}
