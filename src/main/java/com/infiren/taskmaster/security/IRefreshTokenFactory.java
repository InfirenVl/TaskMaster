package com.infiren.taskmaster.security;

public interface IRefreshTokenFactory<Authentication, RefreshToken>  {

    RefreshToken apply(Authentication authentication);
}
