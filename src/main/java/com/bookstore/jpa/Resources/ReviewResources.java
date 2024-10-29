package com.bookstore.jpa.Resources;

import com.bookstore.jpa.Models.Dtos.RequestDTO.ReviewRequestDTO;
import com.bookstore.jpa.Models.Dtos.ReviewDTO;
import com.bookstore.jpa.Models.Review;
import com.bookstore.jpa.Service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/bookstore/reviews")
public class ReviewResources {
    private final ReviewService reviewService;
    @Autowired
    public ReviewResources(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping
    public ResponseEntity<List<ReviewDTO>> listAllReview() {
        List<Review> reviews = reviewService.listAllReview();
        List<ReviewDTO> dto = reviews.stream().map(x -> new ReviewDTO(x.getId(),
                x.getBook().getId(),
                x.getComment())).toList();
        return ResponseEntity.ok().body(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewDTO> findReviewById(@PathVariable UUID id) {
        Review review = reviewService.findReviewById(id);
        ReviewDTO reviewDTO = new ReviewDTO(
                review.getId(),
                review.getBook().getId(),
                review.getComment());
        return ResponseEntity.ok().body(reviewDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReviewDTO> editReview(@PathVariable UUID id, @RequestBody ReviewRequestDTO reviewRequestDTO) {
        Review review = reviewService.editReview(id, reviewRequestDTO);

        ReviewDTO revDTO = new ReviewDTO(
                review.getId(),
                review.getBook().getId(),
                review.getComment());

        return ResponseEntity.ok().body(revDTO);
    }

    @GetMapping("/review_by_bookid")
    public ResponseEntity<ReviewDTO> findReviewByBookId(@RequestParam UUID id) {
        Review review = reviewService.findReviewByBookId(id);

        ReviewDTO reviewDTO = new ReviewDTO(
                review.getId(),
                review.getBook().getId(),
                review.getComment()
        );

        return ResponseEntity.ok().body(reviewDTO);
    }
}
