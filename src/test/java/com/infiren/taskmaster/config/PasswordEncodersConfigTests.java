package com.infiren.taskmaster.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class PasswordEncodersConfigTests {
    public PasswordEncoder userPasswordArgonEncoder(){
        return Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();
    }

    @Test
    public void givenRawPassword_whenEncodedWithArgon2_thenMatchesEncodedPassword(){
        String rawPassword = "QWERTY";
        Argon2PasswordEncoder passwordEncoder = Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();
        String springBouncyHash = passwordEncoder.encode(rawPassword);

        assertTrue(passwordEncoder.matches(rawPassword, springBouncyHash));
    }
}
