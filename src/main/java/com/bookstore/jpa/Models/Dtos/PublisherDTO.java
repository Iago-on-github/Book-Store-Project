package com.bookstore.jpa.Models.Dtos;

import java.util.Set;
import java.util.UUID;

public record PublisherDTO(UUID PublisherId,
                           String name,
                           Set<UUID> books) {
}
