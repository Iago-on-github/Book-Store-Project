package com.bookstore.jpa.Models.Dtos.RequestDTO;

import java.util.Set;
import java.util.UUID;

public record ReviewRequestDTO(UUID book,
                               String comment) {
}
