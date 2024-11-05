package com.bookstore.jpa.Service;

import com.bookstore.jpa.Models.Authors;
import com.bookstore.jpa.Models.Book;
import com.bookstore.jpa.Models.Dtos.RequestDTO.BookRequestDTO;
import com.bookstore.jpa.Models.Dtos.ReviewDTO;
import com.bookstore.jpa.Models.Publisher;
import com.bookstore.jpa.Models.Review;
import com.bookstore.jpa.Repositories.AuthorsRepository;
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

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {
    @Mock
    private BookRepository bookRepository;
    @Mock
    private AuthorsRepository authorsRepository;
    @Mock
    private PublisherRepository publisherRepository;
    @InjectMocks //criar uma instancia e injetar os mocks
    private BookService bookService;
    @Captor
    private ArgumentCaptor<Book> bookArgumentCaptor;
    @Captor
    private ArgumentCaptor<UUID> uuidArgumentCaptor;

    // Arrange
    // Act
    // Assert

    // IMPLEMENTAR TESTES PARA SAVEBOOK 30:32
    @Nested
    class getBookById {
        @Test
        @DisplayName("Should return book by your id")
        void findGetBookByIdWithSuccess() {
            // Arrange
            Book book = new Book(
                    UUID.randomUUID(),
                    "The Rediwnd",
                    new Publisher(UUID.randomUUID(), "Yusm Books"),
                    new Review(UUID.randomUUID(), "Amazing book", null)
            );
            doReturn(Optional.of(book)).when(bookRepository).findById(uuidArgumentCaptor.capture());

            // Act
            var output = bookService.findBookById(book.getId());

            // Assert
            assertNotNull(output);
            assertEquals(output.getId(), uuidArgumentCaptor.getValue());
        }

        @Test
        @DisplayName("Should throws exception when error occurs in get book by id")
        void shouldThrowsExceptionWhenErrorOccursInGetBookById() {
            // Arrange
            UUID bookId = UUID.randomUUID();
            when(bookRepository.findById(bookId)).thenThrow(new RuntimeException());

            // Act & Assert
            assertThrows(RuntimeException.class, () -> bookService.findBookById(bookId));
        }
    }
    @Nested
    class getAllBooks {
        @Test
        @DisplayName("Should return all Books with success")
        void getAllBooksWithSuccess() {

            // Arrange
            Book book = new Book(
                    UUID.randomUUID(),
                    "The Rediwnd",
                    new Publisher(UUID.randomUUID(), "Yusm Books"),
                    new Review(UUID.randomUUID(), "Amazing book", null)
            );
            var list = List.of(book);
            doReturn(list).when(bookRepository)
                    .findAll();

            // Act
            var output = bookService.findAllBooks();

            // Assert
            assertNotNull(output);
            assertEquals(list.size(), output.size());
        }

        @Test
        @DisplayName("Should throws exception when error occurs in Get All Books")
        void shouldThrowsExceptionWhenErrorOccursInGetAllBooks() {
            // Arrange
            when(bookRepository.findAll()).thenThrow(new RuntimeException());

            // Act & Assert
            assertThrows(RuntimeException.class, () -> bookService.findAllBooks());
        }
    }

    @Nested
    class findBookByTitle {
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
        @DisplayName("Should throw exception when error occurs")
        void shouldThrowsExceptionWhenErrorOccursInFindBookByTitle() {
            // Arrange
            String title = "Psycho";
            Book expectedBook = new Book();
            expectedBook.setTitle(title);
            when(bookRepository.findBookByTitle(title)).thenThrow(new RuntimeException());

            // Act & Assert
            assertThrows(RuntimeException.class, () -> bookService.findBookByTitle(title));
        }
    }

    @Nested
    class findBookByPublisherId {
        @Test
        @DisplayName("Should return Book By Publisher ID")
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

        @Test
        @DisplayName("Should throw exception when error occurs")
        void shouldThrowsExceptionWhenErrorOccursInFindBooksByPublisherId() {
            // Arrange
            UUID publisherId = UUID.randomUUID();
            Publisher expectedPublisher = new Publisher(publisherId, "Exthrows Books");
            Book expectedBook = new Book();
            expectedBook.setPublisher(expectedPublisher);

            List<Book> books = new ArrayList<>();
            books.add(expectedBook);

            when(bookRepository.findBooksByPublisherId(publisherId)).thenThrow(new RuntimeException());

            // Act & Assert
            assertThrows(RuntimeException.class, () -> bookService.findBooksByPublisherId(publisherId));
        }
    }

    @Nested
    class saveBook {
        @Test
        @DisplayName("Should a save book with success")
        void saveBookWithSuccess() {

            // Arrange
            UUID publisherId = UUID.randomUUID();
            UUID authorsId = UUID.randomUUID();
            Publisher publisher = new Publisher(publisherId, "Indow Books");
            Authors authors = new Authors(authorsId, "Kvjnsky");
            ReviewDTO reviewDTO = new ReviewDTO(null, null, "Amazing book!");

            BookRequestDTO dto = new BookRequestDTO(
                    "Angels of Dark",
                    publisher.getId(),
                    Set.of(authors.getId()),
                    reviewDTO
            );

            Book book = new Book(
                    UUID.randomUUID(),
                    dto.title(),
                    publisher,
                    new Review(null, dto.reviewComment().comment(), null)
            );

            when(publisherRepository.findById(publisherId)).thenReturn(Optional.of(publisher));
            when(authorsRepository.findById(authorsId)).thenReturn(Optional.of(authors));
            when(bookRepository.save(any(Book.class))).thenReturn(book);

            // Act
            var output = bookService.saveBook(dto);

            // Assert
            assertNotNull(output, "Output should not be null");
            assertEquals(book.getTitle(), output.getTitle(), "Book title should match");
            assertEquals(book.getPublisher(), output.getPublisher(), "Publisher should match");
            assertEquals(book.getAuthors(), output.getAuthors(), "Authors should match");
            assertEquals(book.getReview().getComment(), output.getReview().getComment(), "Review comment should match");

            verify(bookRepository).save(bookArgumentCaptor.capture());
            Book capturedBook = bookArgumentCaptor.getValue();

            assertEquals(output.getTitle(), capturedBook.getTitle(), "Captured book title should match");
            assertEquals(output.getPublisher(), capturedBook.getPublisher(), "Captured Publisher should match");
            //assertEquals(output.getAuthors(), capturedBook.getAuthors(), "Captured Authors should match");
            assertEquals(output.getReview().getComment(), capturedBook.getReview().getComment(), "Captured Review comment should match");
        }

        @Test
        @DisplayName("Should throws exception when error occurs in save book")
        void shouldThrowsExceptionWhenErrorOccursInSaveBook() {
            // Arrange
            UUID publisherId = UUID.randomUUID();
            UUID authorsId = UUID.randomUUID();
            Publisher publisher = new Publisher(publisherId, "Indow Books");
            Authors authors = new Authors(authorsId, "Kvjnsky");
            ReviewDTO reviewDTO = new ReviewDTO(null, null, "Amazing book!");

            BookRequestDTO dto = new BookRequestDTO(
                    "Angels of Dark",
                    publisher.getId(),
                    Set.of(authors.getId()),
                    reviewDTO
            );

            Book book = new Book(
                    UUID.randomUUID(),
                    dto.title(),
                    publisher,
                    new Review(null, dto.reviewComment().comment(), null)
            );

            when(publisherRepository.findById(publisherId)).thenReturn(Optional.empty());

            // Act e Assert
            assertThrows(ResourceNotFoundException.class, () -> bookService.saveBook(dto));
        }
    }
}