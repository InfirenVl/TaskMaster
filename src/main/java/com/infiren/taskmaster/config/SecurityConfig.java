package com.infiren.taskmaster.config;

import com.infiren.taskmaster.security.DefaultAccessTokenFactory;
import com.infiren.taskmaster.security.DefaultRefreshTokenFactory;
import com.infiren.taskmaster.security.jwt.*;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.crypto.DirectDecrypter;
import com.nimbusds.jose.crypto.DirectEncrypter;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;

import java.text.ParseException;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public JwtAuthenticationConfigurer jwtAuthenticationConfigurer(
            @Value("${jwt.path-pattern}") String pathPattern,
            @Value("${jwt.access-token-key}") String accessTokenKey,
            @Value("${jwt.refresh-token-key}") String refreshTokenKey,
            JdbcTemplate jdbcTemplate

    ) throws ParseException, JOSEException {
        return new JwtAuthenticationConfigurer()
                .requestMatcher(PathPatternRequestMatcher.pathPattern(HttpMethod.POST, pathPattern))
                .securityContextRepository(new RequestAttributeSecurityContextRepository())
                .accessTokenFactory(new DefaultAccessTokenFactory())
                .refreshTokenFactory(new DefaultRefreshTokenFactory())
                .accessTokenSerializer(new AccessTokenJwsStringSerializer(
                        new MACSigner(OctetSequenceKey.parse(accessTokenKey))))
                .refreshTokenSerializer(new RefreshTokenJweStringSerializer(
                        new DirectEncrypter(OctetSequenceKey.parse(refreshTokenKey))
                ))
                .accessTokenStringDeserializer(new AccessTokenJwsStringDeserializer(
                        new MACVerifier(OctetSequenceKey.parse(accessTokenKey))
                ))
                .refreshTokenStringDeserializer(new RefreshTokenJweStringDeserializer(
                        new DirectDecrypter(OctetSequenceKey.parse(refreshTokenKey))
                ))
                .jdbcTemplate(jdbcTemplate);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JwtAuthenticationConfigurer jwtAuthenticationConfigurer
    ) throws Exception {

        http.apply(jwtAuthenticationConfigurer);

        return http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorizeHttpRequests ->
                        authorizeHttpRequests
                                .requestMatchers("/error", "/auth/**","/").permitAll()
                                .requestMatchers("/tasks/**").hasRole("USER")
                                .anyRequest().authenticated())
                .build();
    }

}
