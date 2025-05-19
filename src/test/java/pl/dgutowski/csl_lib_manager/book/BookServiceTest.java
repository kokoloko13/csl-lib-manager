package pl.dgutowski.csl_lib_manager.book;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.dgutowski.csl_lib_manager.book.dto.BookDTO;
import pl.dgutowski.csl_lib_manager.exception.ExceptionMessage;
import pl.dgutowski.csl_lib_manager.exception.ResourceNotFoundException;
import pl.dgutowski.csl_lib_manager.utils.DtoUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BookServiceTest {

    private final BookRepository bookRepository = mock(BookRepository.class);

    private final BookService bookService = new BookService(bookRepository);

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
    void getBookById_BookExists() {
        when(bookRepository.findById(any())).thenReturn(Optional.of(TEST_BOOK));
        BookDTO foundBook = bookService.getBookById(TEST_BOOK_ID);

        assertNotNull(foundBook);
        assertEquals(TEST_BOOK_DTO.getId(), foundBook.getId());
        assertEquals(TEST_BOOK_DTO.getTitle(), foundBook.getTitle());
        assertEquals(TEST_BOOK_DTO.getAuthor(), foundBook.getAuthor());
        assertEquals(TEST_BOOK_DTO.getPubYear(), foundBook.getPubYear());
    }

    @Test
    void getBookById_BookNotFound() {
        when(bookRepository.findById(any())).thenThrow(new ResourceNotFoundException(ExceptionMessage.RESOURCE_NOT_FOUND.formatted("Book", TEST_BOOK_ID)));

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> bookService.getBookById(TEST_BOOK_ID));
        assertEquals("Book with id %s not found".formatted(TEST_BOOK_ID), exception.getMessage());
    }

    @Test
    void getListOfAllBooks() {
        when(bookRepository.findAll()).thenReturn(Collections.emptyList());

        List<BookDTO> foundBookDtoList = bookService.getListOfAllBooks();

        assertNotNull(foundBookDtoList);
        assertTrue(foundBookDtoList.isEmpty());
    }

    @Test
    void createNewBook() {
        when(bookRepository.save(any())).thenReturn(TEST_BOOK);

        BookDTO savedBookDto = bookService.createNewBook(TEST_BOOK_DTO);

        assertNotNull(savedBookDto);
        assertNotNull(savedBookDto.getId());
        assertEquals(TEST_BOOK_DTO.getTitle(), savedBookDto.getTitle());
        assertEquals(TEST_BOOK_DTO.getAuthor(), savedBookDto.getAuthor());
        assertEquals(TEST_BOOK_DTO.getPubYear(), savedBookDto.getPubYear());
    }

    @Test
    void updateBook_BookExists() {
        when(bookRepository.findById(TEST_BOOK_ID)).thenReturn(Optional.of(TEST_BOOK));
        when(bookRepository.save(any(Book.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        BookDTO updatedBookBody = BookDTO.builder()
                .id(TEST_BOOK_ID)
                .title(TEST_TITLE)
                .author(TEST_AUTHOR)
                .pubYear("1111").build();

        BookDTO updatedBook = bookService.updateBook(updatedBookBody);

        assertNotNull(updatedBook);
        assertEquals(TEST_BOOK_DTO.getId(), updatedBook.getId());
        assertEquals(TEST_BOOK_DTO.getTitle(), updatedBook.getTitle());
        assertEquals(TEST_BOOK_DTO.getAuthor(), updatedBook.getAuthor());
        assertEquals("1111", updatedBook.getPubYear());
    }

    @Test
    void updateBook_BookNotFound() {
        when(bookRepository.findById(any())).thenThrow(new ResourceNotFoundException(ExceptionMessage.RESOURCE_NOT_FOUND.formatted("Book", TEST_BOOK_ID)));

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> bookService.getBookById(TEST_BOOK_ID));
        assertEquals("Book with id %s not found".formatted(TEST_BOOK_ID), exception.getMessage());
    }
}