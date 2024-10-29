package com.bookstore.jpa.Models.Dtos;

import java.util.UUID;

public record ReviewDTO(UUID reviewId,
                        UUID book,
                        String comment) {
}
