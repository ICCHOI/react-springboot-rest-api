package com.example.gccoffee.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class EmailTest {

    @Test
    void testInvalidEmail() {
        assertThrows(IllegalArgumentException.class, () -> {
            var email = new Email("acccc");
        });
    }

    @Test
    void testValidEmail() {
        var email = new Email("hello@gmail.com");
        assertThat(email.getAddress()).isEqualTo("hello@gmail.com");
    }

    @Test
    void testEqualityEmail() {
        var email = new Email("hello@gmail.com");
        var email2 = new Email("hello@gmail.com");
        assertThat(email.getAddress()).isEqualTo(email2.getAddress());
    }
}