package pl.dgutowski.csl_lib_manager.book;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.dgutowski.csl_lib_manager.book.dto.BookDTO;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/book")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getBook(@Validated @PathVariable UUID id) {
        BookDTO foundBook = bookService.getBookById(id);
        return ResponseEntity.ok(foundBook);
    }

    @GetMapping
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        List<BookDTO> bookList = bookService.getListOfAllBooks();
        return ResponseEntity.ok(bookList);
    }

    @PostMapping("/add")
    public ResponseEntity<BookDTO> addBook(@Valid @RequestBody BookDTO body) {
        BookDTO createdBook = bookService.createNewBook(body);
        return ResponseEntity.status(201).body(createdBook);
    }

    @PutMapping
    public ResponseEntity<BookDTO> updateBook(@Valid @RequestBody BookDTO body) {
        BookDTO updatedBook = bookService.updateBook(body);
        return ResponseEntity.ok(updatedBook);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteBook(@Validated @PathVariable UUID id) {
        bookService.removeBook(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
