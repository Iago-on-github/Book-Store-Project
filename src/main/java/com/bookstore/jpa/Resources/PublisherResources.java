package com.bookstore.jpa.Resources;

import com.bookstore.jpa.Models.Book;
import com.bookstore.jpa.Models.Dtos.PublisherDTO;
import com.bookstore.jpa.Models.Dtos.RequestDTO.PublisherRequestDTO;
import com.bookstore.jpa.Models.Publisher;
import com.bookstore.jpa.Service.PublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/bookstore/publishers")
public class PublisherResources {
    private final PublisherService publisherService;
    @Autowired
    public PublisherResources(PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    @GetMapping
    public ResponseEntity<List<PublisherDTO>> listAllPublishers() {
        List<Publisher> publishers = publisherService.findAllPublishers();
        List<PublisherDTO> publisherDTO = publishers.stream().map(x -> new PublisherDTO(x.getId(),
                x.getName(), x.getBooks().stream().map(Book::getId).collect(Collectors.toSet()))).toList();
        return ResponseEntity.ok().body(publisherDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PublisherDTO> findPublisherByid(@PathVariable UUID id) {
        Publisher publisher = publisherService.findPublisherById(id);
        PublisherDTO publisherDTO = new PublisherDTO(publisher.getId(),
                publisher.getName(),
                publisher.getBooks().stream().map(Book::getId).collect(Collectors.toSet()));
        return ResponseEntity.ok().body(publisherDTO);
    }

    @PostMapping
    public ResponseEntity<PublisherDTO> savePublisher(@RequestBody PublisherRequestDTO publisherRequestDTO, UriComponentsBuilder componentsBuilder) {
        Publisher publisher = publisherService.savePublisher(publisherRequestDTO);

        PublisherDTO publiDTO = new PublisherDTO(
                publisher.getId(),
                publisher.getName(),
                publisher.getBooks().stream().map(Book::getId).collect(Collectors.toSet()));

        URI uri = componentsBuilder.path("/{id}").buildAndExpand(publisher.getId()).toUri();
        return ResponseEntity.created(uri).body(publiDTO);
    }

    @GetMapping("/publisher_name")
    public ResponseEntity<PublisherDTO> findByPublisherName(@RequestParam String name) {
        Publisher publisher = publisherService.findByName(name);

        PublisherDTO publisherDTO = new PublisherDTO(
                publisher.getId(),
                publisher.getName(),
                publisher.getBooks().stream().map(Book::getId).collect(Collectors.toSet())
        );

        return ResponseEntity.ok().body(publisherDTO);
    }

    @GetMapping("/publisher_bookid")
    public ResponseEntity<List<PublisherDTO>> findPublisherByBookId(@RequestParam UUID id) {
        List<Publisher> publishers = publisherService.findPublisherByBookId(id);

        List<PublisherDTO> publisherDTO = publishers.stream()
                .map(x -> new PublisherDTO(
                        x.getId(),
                        x.getName(),
                        x.getBooks().stream().map(Book::getId)
                                .collect(Collectors.toSet()))).toList();

        return ResponseEntity.ok().body(publisherDTO);
    }
}
