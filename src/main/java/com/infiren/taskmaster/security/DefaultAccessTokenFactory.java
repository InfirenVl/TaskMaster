package com.infiren.taskmaster.security;

import java.time.Duration;
import java.time.Instant;

public class DefaultAccessTokenFactory implements IAccessToken<RefreshToken, AccessToken> {

    private final Duration durationTtl = Duration.ofMinutes(5);

    @Override
    public AccessToken apply(RefreshToken refreshToken) {
        Instant now = Instant.now();

        return new AccessToken(refreshToken.id(), refreshToken.subject(),
                refreshToken.authorities().stream()
                        .filter(authority -> authority.startsWith("GRANT_"))
                        .map(authority -> authority.replace("GRANT_", ""))
                        .toList(), now, now.plus(this.durationTtl));
    }

}
