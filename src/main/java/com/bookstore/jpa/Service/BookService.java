package com.bookstore.jpa.Service;

import com.bookstore.jpa.infra.Exceptions.ObjectNotFoundException;
import com.bookstore.jpa.infra.Exceptions.ResourceNotFoundException;
import com.bookstore.jpa.Models.Authors;
import com.bookstore.jpa.Models.Book;
import com.bookstore.jpa.Models.Dtos.RequestDTO.BookRequestDTO;
import com.bookstore.jpa.Models.Review;
import com.bookstore.jpa.Repositories.AuthorsRepository;
import com.bookstore.jpa.Repositories.BookRepository;
import com.bookstore.jpa.Repositories.PublisherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final AuthorsRepository authorsRepository;
    private final PublisherRepository publisherRepository;
    @Autowired
    public BookService(BookRepository bookRepository, AuthorsRepository authorsRepository, PublisherRepository publisherRepository) {
        this.bookRepository = bookRepository;
        this.authorsRepository = authorsRepository;
        this.publisherRepository = publisherRepository;
    }

    public List<Book> findAllBooks() {
        return bookRepository.findAll();
    }

    public Book findBookById(UUID id) {
        Optional<Book> book = bookRepository.findById(id);
        return book.orElseThrow(() -> new ObjectNotFoundException("Book not found"));
    }

    @Transactional
    public Book saveBook(BookRequestDTO dto) {
        Book book = new Book();
        book.setTitle(dto.title());

        book.setPublisher(publisherRepository.findById(dto.publisherId())
                .orElseThrow(() -> new ResourceNotFoundException("Publisher not found")));

        Set<Authors> authors = dto.authorIds().stream()
                .map(authorId -> authorsRepository.findById(authorId)
                        .orElseThrow(() -> new ResourceNotFoundException("Author not found")))
                .collect(Collectors.toSet());
        book.setAuthors(authors);


        Review review = new Review();
        review.setComment(dto.reviewComment().comment());
        review.setBook(book);
        book.setReview(review);

        return bookRepository.save(book);
    }

    public Book findBookByTitle(String title) {
        return bookRepository.findBookByTitle(title);
    }

    public List<Book> findBooksByPublisherId(UUID id) {
        List<Book> books = bookRepository.findBooksByPublisherId(id);
        return books;
    }
}
