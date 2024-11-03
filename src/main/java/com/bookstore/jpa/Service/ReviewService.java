package com.bookstore.jpa.Service;

import com.bookstore.jpa.infra.Exceptions.ResourceNotFoundException;
import com.bookstore.jpa.Models.Dtos.RequestDTO.ReviewRequestDTO;
import com.bookstore.jpa.Models.Review;
import com.bookstore.jpa.Repositories.BookRepository;
import com.bookstore.jpa.Repositories.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    @Autowired
    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
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
