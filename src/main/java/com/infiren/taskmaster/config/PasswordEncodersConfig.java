package com.infiren.taskmaster.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordEncodersConfig {

    //Argon2PasswordEncoder(int saltLength, int hashLength, int parallelism, int memory, int iterations)
    @Bean
    public PasswordEncoder userPasswordArgonEncoder(){
        return Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();
    }

//    @Bean
//    public PasswordEncoder userPasswordBCryptEncoder(){
//        return new BCryptPasswordEncoder(8);
//    }


}
