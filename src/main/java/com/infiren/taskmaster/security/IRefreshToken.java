package com.infiren.taskmaster.security;

public interface IRefreshToken<Authentication, RefreshToken>  {

    RefreshToken apply(Authentication authentication);
}
