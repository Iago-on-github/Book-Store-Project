package com.bookstore.jpa.Service;

import com.bookstore.jpa.Models.Book;
import com.bookstore.jpa.Models.Publisher;
import com.bookstore.jpa.Repositories.BookRepository;
import org.assertj.core.api.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class) //permite o JUnit usar o Mockito
class BookServiceTest {
    @Mock
    private BookRepository bookRepository;
    @InjectMocks //criar uma instancia e injetar os mocks
    private BookService bookService;

    // Arrange
    // Act
    // Assert

    @Test
    @DisplayName("Should return Book by Title")
    void findBookByTitleWithSuccess() {
        // Arrange
        String title = "Senhor dos Anéis";
        Book expectedBook = new Book();
        expectedBook.setTitle(title);
        when(bookRepository.findBookByTitle(title)).thenReturn(expectedBook);

        // Act
        var output = bookService.findBookByTitle(title);

        // Assert
        assertNotNull(output);
        //verifica se o title é retornado igual esperado
        assertEquals(expectedBook.getTitle(), output.getTitle());
    }

    @Test
    void findBooksByPublisherId() {

        // Arrange
        UUID publisherId = UUID.randomUUID();
        Publisher expectedPublisher = new Publisher(publisherId, "Exthrows Books");
        Book expectedBook = new Book();
        expectedBook.setPublisher(expectedPublisher);

        List<Book> books = new ArrayList<>();
        books.add(expectedBook);

        when(bookRepository.findBooksByPublisherId(publisherId)).thenReturn(books);

        // Act
        List<Book> output = bookService.findBooksByPublisherId(publisherId);

        // Assert
        assertNotNull(output);
        assertEquals(expectedBook.getPublisher().getId(), output.get(0).getPublisher().getId());
    }
}