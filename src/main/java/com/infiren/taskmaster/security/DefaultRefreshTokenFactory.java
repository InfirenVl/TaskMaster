package com.infiren.taskmaster.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.time.Duration;
import java.time.Instant;
import java.util.LinkedList;
import java.util.UUID;

public class DefaultRefreshTokenFactory implements IRefreshToken<Authentication, RefreshToken> {

    private final Duration tokenTtl = Duration.ofDays(1);

    @Override
    public RefreshToken apply(Authentication authentication) {
        LinkedList<String> authorities = new LinkedList<>();
        authorities.add("JWT_REFRESH");
        authorities.add("JWT_LOGOUT");
        authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .map(authority -> "GRANT_" + authority)
                .forEach(authorities::add);

        Instant now = Instant.now();
        return new RefreshToken(UUID.randomUUID(), authentication.getName(), authorities, now, now.plus(this.tokenTtl));
    }
}
