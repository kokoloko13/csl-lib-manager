package pl.dgutowski.csl_lib_manager.utils;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import pl.dgutowski.csl_lib_manager.book.Book;
import pl.dgutowski.csl_lib_manager.book.dto.BookDTO;
import pl.dgutowski.csl_lib_manager.user.User;
import pl.dgutowski.csl_lib_manager.user.UserRole;
import pl.dgutowski.csl_lib_manager.user.dto.UserDTO;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class DtoUtilsTest {

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Test
    void userToDto() {
        User user = User.builder()
                .id(UUID.randomUUID())
                .email("test@test.pl")
                .password(encoder.encode("TestPassword!"))
                .role(UserRole.USER)
                .name("Test name")
                .build();

        UserDTO userDTO = DtoUtils.userToDto(user);

        assertNotNull(userDTO);
        assertEquals(user.getName(), userDTO.getName());
        assertEquals(user.getEmail(), userDTO.getEmail());
    }

    @Test
    void bookToDto() {
        Book book = Book.builder()
                .id(UUID.randomUUID())
                .title("Test title")
                .author("Test author")
                .pubYear("1234")
                .build();

        BookDTO bookDTO = DtoUtils.bookToDto(book);

        assertNotNull(bookDTO);
        assertEquals(book.getTitle(), bookDTO.getTitle());
        assertEquals(book.getAuthor(), bookDTO.getAuthor());
        assertEquals(book.getPubYear(), bookDTO.getPubYear());
    }
}