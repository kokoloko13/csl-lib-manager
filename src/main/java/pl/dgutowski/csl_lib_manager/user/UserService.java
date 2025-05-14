package pl.dgutowski.csl_lib_manager.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pl.dgutowski.csl_lib_manager.exception.ExceptionResponse;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::toUserDTO).toList();
    }

    public UserDTO createNewUser(UserDTO body) {
        if(userRepository.existsByEmail(body.getEmail())) {
            throw new ExceptionResponse(HttpStatus.CONFLICT, "REGISTRATION_FAILED");
        }

        User saved = userRepository.save(
                User.builder()
                        .email(body.getEmail())
                        .password("")
                        .name(!body.getName().isBlank() ? body.getName() : null)
                        .build()
        );

        return toUserDTO(saved);
    }

    public UserDTO toUserDTO(User user) {
        return UserDTO.builder()
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }

}
