package com.example.core.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 * Global Exception Handler.
 */
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    /**
     * handleBookmarkNotFoundException.
     * @param e
     * @return ProblemDetail
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    ProblemDetail handleBookmarkNotFoundException(
            final SQLIntegrityConstraintViolationException e) {
        ProblemDetail problemDetail = ProblemDetail
                .forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
        problemDetail.setTitle("Bookmark Not Found");
        problemDetail.setType(
                URI.create("https://api.bookmarks.com/errors/not-found"));
        return problemDetail;
    }
}
