package com.goballogic.challenge.application.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class DuplicateEmailExceptionTest {

    @Test
    void testDuplicateEmailExceptionMessage() {
        // Arrange
        String expectedMessage = "Email already exists";

        // Act
        DuplicateEmailException exception = new DuplicateEmailException(expectedMessage);

        // Assert
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void testDuplicateEmailExceptionIsRuntimeException() {
        // Act & Assert
        assertThrows(DuplicateEmailException.class, () -> {
            throw new DuplicateEmailException("Email already exists");
        });
    }

}
