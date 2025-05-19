package pl.dgutowski.csl_lib_manager.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.dgutowski.csl_lib_manager.user.dto.UserDTO;
import pl.dgutowski.csl_lib_manager.utils.DtoUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(DtoUtils::userToDto).toList();
    }
}
