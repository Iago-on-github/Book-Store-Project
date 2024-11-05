package com.bookstore.jpa.Service;

import com.bookstore.jpa.Models.Book;
import com.bookstore.jpa.Models.Dtos.RequestDTO.PublisherRequestDTO;
import com.bookstore.jpa.Models.Publisher;
import com.bookstore.jpa.Repositories.BookRepository;
import com.bookstore.jpa.Repositories.PublisherRepository;
import com.bookstore.jpa.infra.Exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;


import java.util.*;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PublisherServiceTest {
    @Mock
    private PublisherRepository publisherRepository;
    @Mock
    private BookRepository bookRepository;
    @InjectMocks
    private PublisherService publisherService;
    @Captor
    private ArgumentCaptor<UUID> uuidPublisher;
    @Captor
    private ArgumentCaptor<Publisher> publisherArgumentCaptor;
    @Nested
    class getAllPublishers {
        @Test
        @DisplayName("Should return all Publishers with success")
        void getAllPublishersWithSuccess() {

            // Arrange
            UUID publisherId = UUID.randomUUID();
            Publisher publisher = new Publisher(publisherId, "Nexted Books");

            var list = List.of(publisher);
            when(publisherRepository.findAll()).thenReturn(list);

            // Act
            var output = publisherService.findAllPublishers();

            // Assert
            assertNotNull(output);
            assertEquals(output.size(), list.size());
        }

        @Test
        @DisplayName("Should throews exception when error occurs in Get All Publisher")
        void ShouldThrowsExceptionWhenErrorOccursInGetAllPublisher() {

            // Arrange
            when(publisherRepository.findAll()).thenThrow(new RuntimeException());

            // Act & Assert
            assertThrows(RuntimeException.class, () -> publisherService.findAllPublishers());
        }
    }

    @Nested
    class getPublisherById {
        @Test
        @DisplayName("Should return Publisher by your ID with success")
        void getPublisherByIdWithSuccess() {

            // Arrange
            UUID publisherId = UUID.randomUUID();
            Publisher publisher = new Publisher(publisherId, "Expected Publisher");

            // Act
            doReturn(Optional.of(publisher)).when(publisherRepository)
                    .findById(uuidPublisher.capture());

            // Assert
            var output = publisherRepository.findById(publisherId);

            assertNotNull(output.isPresent());
            assertEquals(publisher, output.get(), "Returned publisher should the expected one");
            assertEquals(publisher.getId(), uuidPublisher.getValue(), "Captured id should match the request ID");
        }

        @Test
        @DisplayName("Should throws exception when error occurs")
        void shouldThrowsExceptionWhenErrorOccurs() {

            // Arrange
            UUID publisherId = UUID.randomUUID();

            when(publisherRepository.findById(publisherId)).thenThrow(new RuntimeException());

            // Assert
            assertThrows(RuntimeException.class,
                    () -> publisherService.findPublisherById(publisherId),
                    "Expected RuntimeException to be throw when an error occurs in findById");

        }
    }

    @Nested
    class getPublisherByName {
        @Test
        @DisplayName("Should get publisher by name with success")
        void shouldGetPublisherByNameWithSuccess() {

            // Arrange
            String publisherName = "Expected Publisher";
            Publisher publisher = new Publisher(null, publisherName);

            doReturn(publisher).when(publisherRepository).findByName(publisherName);

            // Act
            var output = publisherService.findByName(publisherName);

            // Assert
            assertNotNull(output, "output should not be null");
            assertEquals(publisherName, output.getName(), "should return the same name");
        }

        @Test
        @DisplayName("Should throws exception when error occurs in method find by publisher name")
        void shouldThrowsExceptionWhenErrorOccursInMethodFindByPublisherName() {

            // Arrange
            String publisherName = "Expected Publisher";
            when(publisherRepository.findByName(publisherName)).thenThrow(new RuntimeException());

            // Assert
            assertThrows(RuntimeException.class, () -> publisherService.findByName(publisherName),
                    "Expected RuntimeException to be throw an error occurs in findByName");
        }
    }

    @Nested
    class getPublisherByBookIdWithSuccess {
        @Test
        @DisplayName("should get publisher by book id with success")
        void shouldGetPublisherByBookIdWithSuccess() {

            // Arrange
            UUID bookId = UUID.randomUUID();
            Publisher expectedPublisher = new Publisher();

            List<Publisher> publishers = new ArrayList<>();

            publishers.add(expectedPublisher);
            when(publisherRepository.findPublisherByBookId(bookId)).thenReturn(publishers);

            // Act
            var output = publisherService.findPublisherByBookId(bookId);

            // Assert
            assertNotNull(output, "output should be not null");
            assertEquals(output.size(), publishers.size(), "lists should be the same size");
            assertEquals(expectedPublisher.getId(), output.getFirst().getId(), "IDs should be equals");
        }

        @Test
        @DisplayName("Should throws exception when error occurs in get publisher by book id")
        void shouldThrowsExceptionWhenErrorOccursInGePublisherByBookId() {

            // Arrange
            UUID bookId = UUID.randomUUID();

            when(publisherRepository.findPublisherByBookId(bookId)).thenThrow(new RuntimeException());

            // Act & Assert
            assertThrows(RuntimeException.class, () -> publisherService.findPublisherByBookId(bookId),
                    "Should be return RuntimeException when occurs error in findPublisherByBookId");
        }
    }

    @Nested
    class savePublisher {
        @Test
        @DisplayName("Save Publisher with success")
        void savePublisherWithSuccess() {

            // Arrange
            UUID bookId = UUID.randomUUID();
            Book book = new Book(bookId, null, null, null);

            PublisherRequestDTO dto = new PublisherRequestDTO("Expected Publisher",
                    Set.of(book.getId()));

            Publisher publisher = new Publisher(UUID.randomUUID(), dto.name());

            when(publisherRepository.save(any(Publisher.class))).thenReturn(publisher);
            when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

            // Act
            var output = publisherService.savePublisher(dto);

            // Assert
            assertNotNull(output, "output should be not null");
            assertEquals(publisher.getName(), output.getName(), "Names need are the same");
            assertEquals(publisher.getBooks(), output.getBooks(), "Books need are the same");

            verify(publisherRepository).save(publisherArgumentCaptor.capture());
            Publisher publisherCaptured = publisherArgumentCaptor.getValue();

            assertEquals(output.getName(), publisherCaptured.getName(), "name need are the same");
        }

        @Test
        @DisplayName("Should throws exception when error occurs in save publisher")
        void shouldThrowsExceptionWhenErrorOccursInSavePublisher() {
            // Arrange
            UUID bookId = UUID.randomUUID();
            Book book = new Book(bookId, null, null, null);
            PublisherRequestDTO dto = new PublisherRequestDTO("Expected Publisher",
                    Set.of(book.getId()));

            when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(ResourceNotFoundException.class, () -> publisherService.savePublisher(dto));
        }
    }
}