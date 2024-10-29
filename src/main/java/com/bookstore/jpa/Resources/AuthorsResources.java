package com.bookstore.jpa.Resources;

import com.bookstore.jpa.Models.Authors;
import com.bookstore.jpa.Models.Book;
import com.bookstore.jpa.Models.Dtos.AuthorsDTO;
import com.bookstore.jpa.Models.Dtos.RequestDTO.AuthorsRequestDTO;
import com.bookstore.jpa.Service.AuthorsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/bookstore/authors")
public class AuthorsResources {
    private final AuthorsService authorsService;
    @Autowired
    public AuthorsResources(AuthorsService authorsService) {
        this.authorsService = authorsService;
    }

    @GetMapping
    public ResponseEntity<List<AuthorsDTO>> listAllAuthors() {
        List<Authors> authors = authorsService.listAllAuthors();
        List<AuthorsDTO> dto = authors.stream().map(x -> new AuthorsDTO(x.getId(), x.getName(),
                x.getBooks().stream().map(Book::getId).collect(Collectors.toSet()))).toList();
        return ResponseEntity.ok().body(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorsDTO> findAuthorsById(@PathVariable UUID id) {
        Authors authors = authorsService.findAuthorsById(id);
        AuthorsDTO authorsDTO = new AuthorsDTO(authors.getId(),
                authors.getName(),
                authors.getBooks().stream().map(Book::getId).collect(Collectors.toSet()));
        return ResponseEntity.ok().body(authorsDTO);
    }

    @PostMapping
    public ResponseEntity<AuthorsDTO> saveAuthors(@RequestBody AuthorsRequestDTO authorsRequestDTO, UriComponentsBuilder componentsBuilder) {
        Authors authors = authorsService.saveAuthors(authorsRequestDTO);

        AuthorsDTO authDTO = new AuthorsDTO(authors.getId(),
                authors.getName(),
                authors.getBooks().stream().map(Book::getId).collect(Collectors.toSet()));

        URI uri = componentsBuilder.path("/{id}").buildAndExpand(authors.getId()).toUri();
        return ResponseEntity.created(uri).body(authDTO);
    }

    @GetMapping("/authors_name")
    public ResponseEntity<AuthorsDTO> findAuthorsByName(@RequestParam String name) {
        Authors authors = authorsService.findAuthorsByName(name);

        AuthorsDTO authorsDTO = new AuthorsDTO(
                authors.getId(),
                authors.getName(),
                authors.getBooks().stream().map(Book::getId).collect(Collectors.toSet())
        );

        return ResponseEntity.ok().body(authorsDTO);
    }

    @GetMapping("/authors_by_book")
    public ResponseEntity<List<AuthorsDTO>> findAuthorsByBookId(@RequestParam UUID id) {
        List<Authors> authors = authorsService.findAuthorsByBookId(id);

        List<AuthorsDTO> authorsDTO = authors.stream()
                .map(x -> new AuthorsDTO(
                        x.getId(),
                        x.getName(),
                        x.getBooks().stream().map(Book::getId)
                                .collect(Collectors.toSet()))).toList();

        return ResponseEntity.ok().body(authorsDTO);
    }
}
