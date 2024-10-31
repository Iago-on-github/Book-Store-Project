package com.bookstore.jpa.Models.User;


public record RegisterUserDTO(String login,
                              String password,
                              UserRole role) {
}
