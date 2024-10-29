package com.bookstore.jpa.Resources;

import com.bookstore.jpa.Models.Authors;
import com.bookstore.jpa.Models.Book;
import com.bookstore.jpa.Models.Dtos.AuthorsDTO;
import com.bookstore.jpa.Models.Dtos.BookDTO;
import com.bookstore.jpa.Models.Dtos.PublisherDTO;
import com.bookstore.jpa.Models.Dtos.RequestDTO.BookRequestDTO;
import com.bookstore.jpa.Models.Dtos.ReviewDTO;
import com.bookstore.jpa.Service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/bookstore/books")
public class BookResources {
    private final BookService bookService;

    @Autowired
    public BookResources(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public ResponseEntity<List<BookDTO>> listAllBooks() {
        List<Book> books = bookService.findAllBooks();

        List<BookDTO> booksDTO = books.stream().map(book -> new BookDTO(
                book.getTitle(),

                new PublisherDTO(book.getPublisher().getId(), book.getPublisher().getName(),
                        Set.of(book.getPublisher().getId())),

                book.getAuthors().stream()
                        .map(author -> new AuthorsDTO(author.getId(),
                                author.getName(),
                                Set.of(author.getId())))
                        .collect(Collectors.toSet()),

                new ReviewDTO(
                        book.getId(),
                        book.getReview().getId(),
                        book.getReview().getComment()))).toList();

        return ResponseEntity.ok().body(booksDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> findBooksById(@PathVariable UUID id) {
        Book book = bookService.findBookById(id);

        BookDTO bookDTO = new BookDTO(
                book.getTitle(),

                new PublisherDTO(book.getPublisher().getId(),
                        book.getPublisher().getName(),
                        book.getPublisher().getBooks().stream().map(Book::getId).collect(Collectors.toSet())),

                book.getAuthors().stream().map(x-> new AuthorsDTO(x.getId(),
                        x.getName(),
                        Set.of(x.getId()))).collect(Collectors.toSet()),

                new ReviewDTO(
                        book.getId(),
                        book.getReview().getId(),
                        book.getReview().getComment()));

        return ResponseEntity.ok().body(bookDTO);
    }

    @PostMapping
    public ResponseEntity<BookDTO> saveBook(@RequestBody BookRequestDTO bookRequestDTO, UriComponentsBuilder componentsBuilder) {
        Book book = bookService.saveBook(bookRequestDTO);
        URI uri = componentsBuilder.path("/{id}").buildAndExpand(book.getId()).toUri();

        Set<AuthorsDTO> authorsDTO = book.getAuthors().stream()
                .map(author -> new AuthorsDTO(author.getId(), author.getName(), author.getBooks().stream().map(Book::getId).collect(Collectors.toSet())))
                .collect(Collectors.toSet());

        BookDTO dto = new BookDTO(
                book.getTitle(),
                new PublisherDTO(
                        book.getPublisher().getId(),
                        book.getPublisher().getName(),
                        book.getPublisher().getBooks().stream()
                                .map(Book::getId).collect(Collectors.toSet())),

                authorsDTO,

                new ReviewDTO(
                        book.getId(),
                        book.getId(),
                        book.getReview().getComment()));

        return ResponseEntity.created(uri).body(dto);
    }

    @GetMapping("/book_title")
    public ResponseEntity<BookDTO> findBookByTitle(@RequestParam String title) {
        Book book = bookService.findBookByTitle(title);

        Set<AuthorsDTO> authorsDTO = book.getAuthors().stream()
                .map(author -> new AuthorsDTO(
                        author.getId(), author.getName(),
                        author.getBooks().stream()
                                .map(Book::getId).collect(Collectors.toSet())))
                .collect(Collectors.toSet());

        BookDTO bookDTO = new BookDTO(
                book.getTitle(),
                new PublisherDTO(book.getPublisher().getId(),
                        book.getPublisher().getName(),
                        book.getPublisher().getBooks().stream()
                                .map(Book::getId).collect(Collectors.toSet())),

                authorsDTO,

                new ReviewDTO(book.getId(),
                        book.getId(),
                        book.getReview().getComment()));

        return ResponseEntity.ok().body(bookDTO);
    }

    @GetMapping("/book_publisher")
    public ResponseEntity<List<BookDTO>> findBooksByPublisherId(@RequestParam UUID id) {
        List<Book> books = bookService.findBooksByPublisherId(id);

        List<BookDTO> bookDTOList = books.stream().map(book -> new BookDTO(
                book.getTitle(),

                new PublisherDTO(book.getPublisher().getId(),
                        book.getPublisher().getName(),
                        Set.of(book.getPublisher().getId())),

                book.getAuthors().stream().map(authors -> new AuthorsDTO(
                        authors.getId(),
                        authors.getName(),
                        Set.of(authors.getId())))
                        .collect(Collectors.toSet()),

                new ReviewDTO(book.getId(),
                        book.getReview().getId(),
                        book.getReview().getComment()))).toList();

        return ResponseEntity.ok().body(bookDTOList);
    }
}
