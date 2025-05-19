package pl.dgutowski.csl_lib_manager.utils;

import pl.dgutowski.csl_lib_manager.book.Book;
import pl.dgutowski.csl_lib_manager.book.dto.BookDTO;
import pl.dgutowski.csl_lib_manager.user.User;
import pl.dgutowski.csl_lib_manager.user.dto.UserDTO;

public final class DtoUtils {
    private DtoUtils() {}

    public static UserDTO userToDto(User user) {
        return UserDTO.builder()
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }

    public static BookDTO bookToDto(Book book) {
        return BookDTO.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .pubYear(book.getPubYear())
                .build();
    }

}
