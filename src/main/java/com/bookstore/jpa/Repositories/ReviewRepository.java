package com.bookstore.jpa.Repositories;

import com.bookstore.jpa.Models.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, UUID> {
    @Query(value = "SELECT * FROM tb_review WHERE book_id = :id", nativeQuery = true)
    Review findReviewByBookId(@Param("id") UUID id);
}
