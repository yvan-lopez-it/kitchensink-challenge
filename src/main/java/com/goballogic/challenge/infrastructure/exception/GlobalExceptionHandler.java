package com.goballogic.challenge.infrastructure.exception;

import com.goballogic.challenge.application.exceptions.DuplicateEmailException;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@NoArgsConstructor
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<String> handleDuplicateEmail(DuplicateEmailException ex) {
        // Retorna un código de estado 409 (Conflict) y el mensaje de la excepción.
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

}
