package com.bookstore.jpa.Models.Dtos.RequestDTO;

import java.util.Set;
import java.util.UUID;

public record AuthorsRequestDTO(String name,
                                Set<UUID> books) {
}
