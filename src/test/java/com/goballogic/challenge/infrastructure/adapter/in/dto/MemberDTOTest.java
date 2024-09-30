package com.goballogic.challenge.infrastructure.adapter.in.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


public class MemberDTOTest {

    private static Validator validator;

    @BeforeAll
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidMemberDTO() {
        MemberDTO member = MemberDTO.builder()
            .id("123")
            .name("John Castro")
            .email("john.castro@example.com")
            .phoneNumber("1234567890")
            .build();

        Set<ConstraintViolation<MemberDTO>> violations = validator.validate(member);
        assertTrue(violations.isEmpty(), "There should be no violations for a valid DTO");
    }

    @Test
    void testInvalidName() {
        MemberDTO member = MemberDTO.builder()
            .id("123")
            .name("John123") // Invalid: contains numbers
            .email("john.castro@example.com")
            .phoneNumber("1234567890")
            .build();

        Set<ConstraintViolation<MemberDTO>> violations = validator.validate(member);
        assertEquals(1, violations.size());
    }

    @Test
    void testInvalidEmail() {
        MemberDTO member = MemberDTO.builder()
            .id("123")
            .name("Jose Pardo")
            .email("invalid-email") // Invalid email format
            .phoneNumber("1234567890")
            .build();

        Set<ConstraintViolation<MemberDTO>> violations = validator.validate(member);
        assertEquals(1, violations.size());
    }

    @Test
    void testInvalidPhoneNumber() {
        MemberDTO member = MemberDTO.builder()
            .id("123")
            .name("Carlos Bueno")
            .email("carlos.bueno@example.com")
            .phoneNumber("12345") // Invalid: too short
            .build();

        Set<ConstraintViolation<MemberDTO>> violations = validator.validate(member);
        assertEquals(1, violations.size());
    }

}
