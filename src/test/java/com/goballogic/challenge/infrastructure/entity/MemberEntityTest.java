package com.goballogic.challenge.infrastructure.entity;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MemberEntityTest {

    private final Validator validator;

    public MemberEntityTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void whenValidName_thenNoConstraintViolations() {
        MemberEntity member = new MemberEntity("1", "Alicia", "alice@example.com", "1234567890");

        var violations = validator.validate(member);

        assertThat(violations).isEmpty();
    }

    @Test
    void whenInvalidEmail_thenConstraintViolation() {
        MemberEntity member = new MemberEntity("1", "Alicia", "invalid-email", "1234567890");

        var violations = validator.validate(member);

        assertThat(violations).isNotEmpty();
    }

    @Test
    void whenInvalidPhoneNumber_thenConstraintViolation() {
        MemberEntity member = new MemberEntity("1", "Alicia", "alice@example.com", "123");

        var violations = validator.validate(member);

        assertThat(violations).isNotEmpty();
    }

}
