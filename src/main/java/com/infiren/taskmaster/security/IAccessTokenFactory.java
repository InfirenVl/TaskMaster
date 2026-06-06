package com.infiren.taskmaster.security;

public interface IAccessTokenFactory<RefreshToken, AccessToken> {

    AccessToken apply(RefreshToken refreshToken);
}
