package com.infiren.taskmaster.security;

public interface IAccessToken<RefreshToken, AccessToken> {

    AccessToken apply(RefreshToken refreshToken);
}
