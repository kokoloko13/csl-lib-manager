package pl.dgutowski.csl_lib_manager.book;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.dgutowski.csl_lib_manager.book.dto.BookDTO;
import pl.dgutowski.csl_lib_manager.utils.DtoUtils;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BookControllerTest {

    private final BookService bookService = mock(BookService.class);

    private final BookController bookController = new BookController(bookService);

    private final String TEST_TITLE = "Test title";
    private final String TEST_AUTHOR = "Test author";
    private final String TEST_PUB_YEAR = "0000";

    private UUID TEST_BOOK_ID = UUID.randomUUID();

    private BookDTO TEST_BOOK_DTO;
    private Book TEST_BOOK;

    @BeforeEach
    void setup() {
        TEST_BOOK = Book.builder()
                .id(TEST_BOOK_ID)
                .title(TEST_TITLE)
                .author(TEST_AUTHOR)
                .pubYear(TEST_PUB_YEAR)
                .build();

        TEST_BOOK_DTO = DtoUtils.bookToDto(TEST_BOOK);
    }

    @Test
    void getBook() {
        when(bookService.getBookById(any())).thenReturn(TEST_BOOK_DTO);
        BookDTO bookDTO = bookController.getBook(TEST_BOOK_ID).getBody();

        assertEquals(TEST_BOOK_DTO.getId(), bookDTO.getId());
        assertEquals(TEST_BOOK_DTO.getTitle(), bookDTO.getTitle());
        assertEquals(TEST_BOOK_DTO.getAuthor(), bookDTO.getAuthor());
        assertEquals(TEST_BOOK_DTO.getPubYear(), bookDTO.getPubYear());
    }

    @Test
    void getAllBooks() {
        when(bookService.getListOfAllBooks()).thenReturn(Collections.emptyList());
        List<BookDTO> bookDTOList = bookController.getAllBooks().getBody();

        assertNotNull(bookDTOList);
        assertTrue(bookDTOList.isEmpty());
    }

    @Test
    void addBook() {
        BookDTO newBookBody = BookDTO.builder()
                .title(TEST_TITLE)
                .author(TEST_AUTHOR)
                .pubYear(TEST_PUB_YEAR)
                .build();

        when(bookService.createNewBook(newBookBody)).thenReturn(DtoUtils.bookToDto(TEST_BOOK));

        BookDTO result = bookController.addBook(newBookBody).getBody();

        assertEquals(newBookBody.getTitle(), result.getTitle());
        assertEquals(newBookBody.getAuthor(), result.getAuthor());
        assertEquals(newBookBody.getPubYear(), result.getPubYear());
        assertNotNull(result.getId());
    }

    @Test
    void updateBook() {
        when(bookService.updateBook(any())).thenReturn(TEST_BOOK_DTO);
        BookDTO editedBookDTO = bookController.updateBook(TEST_BOOK_DTO).getBody();

        assertNotNull(editedBookDTO);
        assertEquals(TEST_BOOK_DTO.getId(), editedBookDTO.getId());
        assertEquals(TEST_BOOK_DTO.getTitle(), editedBookDTO.getTitle());
        assertEquals(TEST_BOOK_DTO.getAuthor(), editedBookDTO.getAuthor());
        assertEquals(TEST_BOOK_DTO.getPubYear(), editedBookDTO.getPubYear());
    }

    @Test
    void removeBook() {
        when(bookService.removeBook(any())).thenReturn(true);
        ResponseEntity response = bookController.deleteBook(TEST_BOOK_ID);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}