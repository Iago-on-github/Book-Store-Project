package com.bookstore.jpa.Repositories;

import com.bookstore.jpa.Models.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PublisherRepository extends JpaRepository<Publisher, UUID> {
    Publisher findByName(String name);
    @Query(value = "SELECT a.* FROM tb_publisher a " +
            "JOIN tb_book b ON a.id = b.publisher_id " +
            "WHERE b.id = :id", nativeQuery = true)
    List<Publisher> findPublisherByBookId(@Param("id") UUID id);
}
