package com.infiren.taskmaster.security;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public sealed interface Token permits AccessToken, RefreshToken {
    UUID id();
    String subject();
    List<String> authorities();
    Instant createdAt();
    Instant expiresAt();
}
