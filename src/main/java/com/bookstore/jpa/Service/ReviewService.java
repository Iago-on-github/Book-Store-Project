package com.bookstore.jpa.Service;

import com.bookstore.jpa.Exceptions.ObjectNotFoundException;
import com.bookstore.jpa.Exceptions.ResourceNotFoundException;
import com.bookstore.jpa.Models.Book;
import com.bookstore.jpa.Models.Dtos.RequestDTO.ReviewRequestDTO;
import com.bookstore.jpa.Models.Dtos.ReviewDTO;
import com.bookstore.jpa.Models.Review;
import com.bookstore.jpa.Repositories.BookRepository;
import com.bookstore.jpa.Repositories.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final BookRepository bookRepository;
    @Autowired
    public ReviewService(ReviewRepository reviewRepository, BookRepository bookRepository) {
        this.reviewRepository = reviewRepository;
        this.bookRepository = bookRepository;
    }

    public List<Review> listAllReview() {
        return reviewRepository.findAll();
    }

    public Review findReviewById(UUID id) {
        return reviewRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Review not found"));
    }

    @Transactional
    public Review editReview(UUID reviewId, ReviewRequestDTO reviewRequestDTO) {
        Review review = findReviewById(reviewId);

        review.setComment(reviewRequestDTO.comment());

        return reviewRepository.save(review);
    }

    public Review findReviewByBookId(UUID id) {
        return reviewRepository.findReviewByBookId(id);
    }
}
