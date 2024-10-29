package com.bookstore.jpa.Repositories;

import com.bookstore.jpa.Models.Authors;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AuthorsRepository extends JpaRepository<Authors, UUID> {
    Authors findAuthorsByName(String name);

    @Query(value = "SELECT a.* FROM tb_authors a " +
            "JOIN tb_book_author ba ON a.id = ba.author_id " +
            "WHERE ba.book_id = :id", nativeQuery = true)
    List<Authors> findAuthorsByBookId(@Param("id") UUID id);

}
