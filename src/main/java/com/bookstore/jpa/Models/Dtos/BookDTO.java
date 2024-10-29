package com.bookstore.jpa.Models.Dtos;

import java.util.Set;

public record BookDTO(String title,
                      PublisherDTO publisher,
                      Set<AuthorsDTO> authors,
                      ReviewDTO reviewComment)
{}

