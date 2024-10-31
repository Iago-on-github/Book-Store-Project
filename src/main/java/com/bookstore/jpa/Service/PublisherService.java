package com.bookstore.jpa.Service;

import com.bookstore.jpa.infra.Exceptions.ObjectNotFoundException;
import com.bookstore.jpa.infra.Exceptions.ResourceNotFoundException;
import com.bookstore.jpa.Models.Book;
import com.bookstore.jpa.Models.Dtos.RequestDTO.PublisherRequestDTO;
import com.bookstore.jpa.Models.Publisher;
import com.bookstore.jpa.Repositories.BookRepository;
import com.bookstore.jpa.Repositories.PublisherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PublisherService {
    private final PublisherRepository publisherRepository;
    private final BookRepository bookRepository;
    @Autowired
    public PublisherService(PublisherRepository publisherRepository, BookRepository bookRepository) {
        this.publisherRepository = publisherRepository;
        this.bookRepository = bookRepository;
    }

    public List<Publisher> findAllPublishers() {
        return publisherRepository.findAll();
    }

    public Publisher findPublisherById(UUID id) {
        return publisherRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Publisher not found"));
    }

    @Transactional
    public Publisher savePublisher(PublisherRequestDTO publisherRequestDTO) {
        Publisher publisher = new Publisher();

        publisher.setName(publisherRequestDTO.name());
        Set<Book> bookSet = publisherRequestDTO.books().stream().map(bookId -> bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found")))
                .collect(Collectors.toSet());
        publisher.setBooks(bookSet);

        return publisherRepository.save(publisher);
    }

    public Publisher findByName(String name) {
        return publisherRepository.findByName(name);
    }

    public List<Publisher> findPublisherByBookId(UUID id) {
        List<Publisher> publishers = publisherRepository.findPublisherByBookId(id);
        return publishers;
    }
}
