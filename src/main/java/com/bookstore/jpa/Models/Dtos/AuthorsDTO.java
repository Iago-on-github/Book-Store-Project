package com.bookstore.jpa.Models.Dtos;

import java.util.Set;
import java.util.UUID;

public record AuthorsDTO(UUID AuthorId,
                         String name,
                         Set<UUID> books) {
}
