package com.bookstore.jpa.infra.Exceptions;

public class ObjectNotFoundException extends RuntimeException{
    public ObjectNotFoundException(String msg) {
        super(msg);
    }
}
