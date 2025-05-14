package pl.dgutowski.csl_lib_manager.book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pl.dgutowski.csl_lib_manager.exception.HTTPException;


import java.util.List;
import java.util.UUID;

@Service
public class BookService {

    @Autowired
    BookRepository bookRepository;

    public Book getBookById(UUID id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new HTTPException(HttpStatus.NOT_FOUND, "BOOK_NOT_FOUND"));
    }

    public List<Book> getListOfAllBooks() {
        return bookRepository.findAll();
    }

    public Book createNewBook(Book body) {
        return bookRepository.save(body);
    }

    public Book updateBook(UUID id, Book body) {
        Book foundBook = getBookById(id);

        foundBook.setPubYear(body.getPubYear());
        foundBook.setAuthor(body.getAuthor());
        foundBook.setTitle(body.getTitle());


        return bookRepository.save(foundBook);
    }
}
