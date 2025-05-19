package pl.dgutowski.csl_lib_manager.book;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.dgutowski.csl_lib_manager.book.dto.BookDTO;
import pl.dgutowski.csl_lib_manager.exception.ExceptionMessage;
import pl.dgutowski.csl_lib_manager.exception.ResourceNotFoundException;
import pl.dgutowski.csl_lib_manager.utils.DtoUtils;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    private Book getRawBookById(UUID id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                                ExceptionMessage.RESOURCE_NOT_FOUND.formatted("Book", id.toString())
                ));
    }

    public BookDTO getBookById(UUID id) {
        return DtoUtils.bookToDto(getRawBookById(id));
    }

    public List<BookDTO> getListOfAllBooks() {
        return bookRepository.findAll().stream().map(DtoUtils::bookToDto).toList();
    }

    public BookDTO createNewBook(BookDTO body) {
        Book newBook = Book.builder()
                .title(body.getTitle())
                .author(body.getAuthor())
                .pubYear(body.getPubYear())
                .build();
        return DtoUtils.bookToDto(bookRepository.save(newBook));
    }

    public BookDTO updateBook(BookDTO body) {
        Book foundBook = getRawBookById(body.getId());

        foundBook.setPubYear(body.getPubYear());
        foundBook.setAuthor(body.getAuthor());
        foundBook.setTitle(body.getTitle());


        return DtoUtils.bookToDto(bookRepository.save(foundBook));
    }

    public boolean removeBook(UUID id) {
        Book foundBook = getRawBookById(id);
        bookRepository.deleteById(id);

        return !bookRepository.existsById(id);
    }
}
