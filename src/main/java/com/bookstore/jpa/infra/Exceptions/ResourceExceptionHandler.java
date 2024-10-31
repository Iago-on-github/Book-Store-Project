package com.bookstore.jpa.infra.Exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
@ControllerAdvice
public class ResourceExceptionHandler {
    //fazer com o erro 403 personalizado
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandartError> resourceNotFound(ResourceNotFoundException resourceNotFound, HttpServletRequest httpServlet) {
        String error = "Resource not found";
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandartError standartError = new StandartError(
                Instant.now(),
                status.value(),
                error,
                resourceNotFound.getMessage(),
                httpServlet.getRequestURI());
        return ResponseEntity.ok().body(standartError);
    }
}
