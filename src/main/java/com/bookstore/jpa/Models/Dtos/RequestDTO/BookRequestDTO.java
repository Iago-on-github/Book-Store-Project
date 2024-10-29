package com.bookstore.jpa.Models.Dtos.RequestDTO;


import com.bookstore.jpa.Models.Dtos.ReviewDTO;

import java.util.Set;
import java.util.UUID;

public record BookRequestDTO(
        String title,
        UUID publisherId,
        Set<UUID> authorIds,
        ReviewDTO reviewComment
) {}
