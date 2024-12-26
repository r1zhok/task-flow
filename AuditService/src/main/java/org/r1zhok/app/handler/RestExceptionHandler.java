package org.r1zhok.app.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.NoSuchElementException;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(NoSuchElementException.class)
    ResponseEntity<String> handleNoSuchElementException(NoSuchElementException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}