package com.bookstore.jpa.Repositories;

import com.bookstore.jpa.Models.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;
public interface BookRepository extends JpaRepository<Book, UUID> {
    Book findBookByTitle(String title);
    @Query(value = "SELECT * FROM tb_book WHERE publisher_id = :id", nativeQuery = true)
    List<Book> findBooksByPublisherId(@Param("id") UUID id);
}
