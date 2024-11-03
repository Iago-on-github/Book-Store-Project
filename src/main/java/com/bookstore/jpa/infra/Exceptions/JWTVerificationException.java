package com.bookstore.jpa.infra.Exceptions;

public class JWTVerificationException extends RuntimeException {
    public JWTVerificationException(String msg) {
        super(msg);
    }
}
