package com.bookstore.jpa.Service;

import com.bookstore.jpa.Exceptions.ObjectNotFoundException;
import com.bookstore.jpa.Exceptions.ResourceNotFoundException;
import com.bookstore.jpa.Models.Authors;
import com.bookstore.jpa.Models.Book;
import com.bookstore.jpa.Models.Dtos.AuthorsDTO;
import com.bookstore.jpa.Models.Dtos.RequestDTO.AuthorsRequestDTO;
import com.bookstore.jpa.Repositories.AuthorsRepository;
import com.bookstore.jpa.Repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AuthorsService {
    private final AuthorsRepository authorsRepository;
    private final BookRepository bookRepository;
    @Autowired
    public AuthorsService(AuthorsRepository authorsRepository, BookRepository bookRepository) {
        this.authorsRepository = authorsRepository;
        this.bookRepository = bookRepository;
    }

    public List<Authors> listAllAuthors() {
        return authorsRepository.findAll();
    }

    public Authors findAuthorsById(UUID id) {
        return authorsRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Author not found"));
    }

    @Transactional
    public Authors saveAuthors(AuthorsRequestDTO authorsRequestDTO) {
        Authors authors = new Authors();

        authors.setName(authorsRequestDTO.name());
        Set<Book> bookSet = authorsRequestDTO.books().stream().map(bookId -> bookRepository.findById(bookId)
                        .orElseThrow(() -> new ResourceNotFoundException("Book not found")))
                .collect(Collectors.toSet());
        authors.setBooks(bookSet);
        return authorsRepository.save(authors);
    }

    public Authors findAuthorsByName(String name) {
        return authorsRepository.findAuthorsByName(name);
    }

    public List<Authors> findAuthorsByBookId(UUID id) {
        List<Authors> authors = authorsRepository.findAuthorsByBookId(id);
        return authors;
    }
}
