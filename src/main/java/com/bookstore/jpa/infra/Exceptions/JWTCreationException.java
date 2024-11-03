package com.bookstore.jpa.infra.Exceptions;

public class JWTCreationException extends RuntimeException {
    public JWTCreationException(String msg) {
        super(msg);
    }
}
