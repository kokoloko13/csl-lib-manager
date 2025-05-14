package pl.dgutowski.csl_lib_manager.book;

import org.springframework.beans.factory.annotation.Autowired;
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

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/book")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping("/{id}")
    public ResponseEntity<Book>  getBook(@PathVariable UUID id) {
        Book foundBook = bookService.getBookById(id);
        return ResponseEntity.ok(foundBook);
    }

    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> bookList = bookService.getListOfAllBooks();
        return ResponseEntity.ok(bookList);
    }

    @PostMapping("/add")
    public ResponseEntity<Book> addBook(@Validated @RequestBody Book body) {
        Book createdBook = bookService.createNewBook(body);
        return ResponseEntity.status(201).body(createdBook);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable UUID id, @Validated @RequestBody Book body) {
        Book updatedBook = bookService.updateBook(id, body);
        return ResponseEntity.ok(updatedBook);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Book> deleteBook(@PathVariable UUID id) {
        bookService.removeBook(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
