package pl.dgutowski.csl_lib_manager.user;

import org.junit.jupiter.api.Test;
import pl.dgutowski.csl_lib_manager.user.dto.UserDTO;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserControllerTest {

    private final UserService userService = mock(UserService.class);
    private final UserController userController = new UserController(userService);

    @Test
    void getAllUsers() {
        when(userService.getAllUsers()).thenReturn(Collections.emptyList());
        List<UserDTO> userDTOList = userController.getAllUsers().getBody();

        assertNotNull(userDTOList);
        assertTrue(userDTOList.isEmpty());
    }

}