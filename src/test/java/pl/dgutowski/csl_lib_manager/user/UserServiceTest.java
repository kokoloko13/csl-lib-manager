package pl.dgutowski.csl_lib_manager.user;

import org.junit.jupiter.api.Test;
import pl.dgutowski.csl_lib_manager.user.dto.UserDTO;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserServiceTest {

    private final UserRepository userRepository = mock(UserRepository.class);
    private final UserService userService = new UserService(userRepository);


    @Test
    void getAllUsers() {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());
        List<UserDTO> userDTOList = userService.getAllUsers();

        assertNotNull(userDTOList);
        assertTrue(userDTOList.isEmpty());
    }
}