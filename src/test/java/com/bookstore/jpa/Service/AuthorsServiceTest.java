package com.bookstore.jpa.Service;

import com.bookstore.jpa.Models.Authors;
import com.bookstore.jpa.Models.Book;
import com.bookstore.jpa.Models.Dtos.RequestDTO.AuthorsRequestDTO;
import com.bookstore.jpa.Repositories.AuthorsRepository;
import com.bookstore.jpa.Repositories.BookRepository;
import com.bookstore.jpa.infra.Exceptions.ResourceNotFoundException;
import org.hibernate.ObjectNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.management.RuntimeErrorException;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorsServiceTest {
    @InjectMocks
    private AuthorsService authorsService;
    @Mock
    private AuthorsRepository authorsRepository;
    @Mock
    private BookRepository bookRepository;
    @Captor
    private ArgumentCaptor<UUID> uuidArgumentCaptor;
    @Captor
    private ArgumentCaptor<Authors> authorArgumentCaptor;

    @Nested
    class getAllAuthors {
        @Test
        @DisplayName("Should get all authors with success")
        void shouldGetAllAuthorsWithSuccess() {

            // Arrange
            Authors authors = new Authors();

            var list = List.of(authors);
            doReturn(list).when(authorsRepository).findAll();

            // Act
            var output = authorsService.listAllAuthors();

            // Assert
            assertNotNull(output, "Checking output is not null");
            assertEquals(list.size(), output.size(), "Size of list should be equals");
        }

        @Test
        @DisplayName("Should throws exception when error occurs in get all authors")
        void shouldThrowsExceptionWhenErrorOccursInGetAllAuthors() {

            // Arrange
            when(authorsRepository.findAll()).thenThrow(new RuntimeException());

            // Act & Assert
            assertThrows(RuntimeException.class, () -> authorsService.listAllAuthors());
        }
    }

    @Nested
    class getAuthorsById {
        @Test
        @DisplayName("Should get authors by id with success")
        void shouldGetAuthorsByIdWithSuccess() {

            // Arrange
            UUID authorsId = UUID.randomUUID();
            Authors expectedAuthor = new Authors(authorsId, "Vladmir");
            expectedAuthor.setId(authorsId);

            doReturn(Optional.of(expectedAuthor)).when(authorsRepository).findById(uuidArgumentCaptor.capture());

            // Act
            var output = authorsService.findAuthorsById(expectedAuthor.getId());

            // Assert
            assertNotNull(output);
            assertEquals(uuidArgumentCaptor.getValue(), output.getId());
        }

        @Test
        @DisplayName("Should throws exception when error occurs in get authors by id")
        void shouldThrowsExceptionWhenErrorOccursInGetAuthorsById() {

            // Arrange
            UUID authorsId = UUID.randomUUID();
            when(authorsRepository.findById(authorsId)).thenThrow(new RuntimeException());

            // Act & Asser
            assertThrows(RuntimeException.class, () -> authorsService.findAuthorsById(authorsId));
        }
    }

    @Nested
    class getAuthorsByName {
        @Test
        @DisplayName("Should get authors by name with success")
        void getAuthorsByNameWithSuccess() {
            String authorName = "Expected name";
            Authors authors = new Authors(null, authorName);

            when(authorsRepository.findAuthorsByName(authorName)).thenReturn(authors);

            var output = authorsService.findAuthorsByName(authorName);

            assertNotNull(output, "output should not be null");
            assertEquals(authors.getName(), output.getName(), "Authors name and Output should be equals");
        }

        @Test
        @DisplayName("Should throws exception when error occurs in get authors by name")
        void shouldThrowsExceptionWhenErrorOccursInGetAuthorsByName() {
            String name = "Expected name";

            when(authorsRepository.findAuthorsByName(name)).thenThrow(new RuntimeException());

            assertThrows(RuntimeException.class, () -> authorsService.findAuthorsByName(name));
        }
    }

    @Nested
    class getAuthorsByBookId {
        @Test
        @DisplayName("Should get authors by book id with success")
        void getAuthorsByBookIdWithSuccess() {
            UUID id = UUID.randomUUID();
            Authors author = new Authors(id, null);

            var list = List.of(author);
            when(authorsRepository.findAuthorsByBookId(id)).thenReturn(list);

            List<Authors> output = authorsService.findAuthorsByBookId(author.getId());

            assertNotNull(output, "output should be not null");
            assertEquals(list.size(), output.size(), "The size of 'list' and 'output' need be equals");
            assertEquals(list.getFirst(), output.getFirst(), "The id's need be equals");
        }

        @Test
        @DisplayName("Should throws exception when error occurs in get authors by book id")
        void shouldThrowsExceptionWhenErrorOccursInGetAuthorsByBookId() {
            UUID id = UUID.randomUUID();

            when(authorsRepository.findAuthorsByBookId(id)).thenThrow(new RuntimeException());

            assertThrows(RuntimeException.class, () -> authorsService.findAuthorsByBookId(id));
        }
    }

    @Nested
    class saveAuthors {
        @Test
        @DisplayName("Should save authors with success")
        void saveAuthorsWithSuccess() {
            UUID bookId = UUID.randomUUID();
            Book book = new Book(bookId, null, null, null);

            Set<Book> bookSet = new HashSet<>();
            bookSet.add(book);

            UUID id = bookSet.stream().map(Book::getId).findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Book set is empty"));

            AuthorsRequestDTO dto = new AuthorsRequestDTO(
                    "Expected author",
                    bookSet.stream().map(Book::getId).collect(Collectors.toSet()));

            Authors authors = new Authors(bookId, dto.name());

            when(authorsRepository.save(any(Authors.class))).thenReturn(authors);
            when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

            var output = authorsService.saveAuthors(dto);

            assertNotNull(output);
            assertEquals(authors.getId(), output.getId());
            assertEquals(authors.getName(), output.getName());
            assertEquals(authors.getBooks(), output.getBooks());

            verify(authorsRepository).save(authorArgumentCaptor.capture());
            Authors capturedAuthor = authorArgumentCaptor.getValue();

            assertEquals(capturedAuthor.getName(), output.getName());
        }

        @Test
        @DisplayName("Should throws exception when error occurs in save authors")
        void shouldThrowsExceptionWhenErrorOccursInSaveAuthors() {
            UUID bookId = UUID.randomUUID();
            Book book = new Book(bookId, null, null, null);

            Set<Book> bookSet = new HashSet<>();
            bookSet.add(book);

            AuthorsRequestDTO dto = new AuthorsRequestDTO(
                    "Expected author",
                    bookSet.stream().map(Book::getId).collect(Collectors.toSet()));


            when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () -> authorsService.saveAuthors(dto));
        }
    }
}