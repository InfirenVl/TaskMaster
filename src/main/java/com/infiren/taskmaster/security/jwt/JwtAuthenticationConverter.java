package com.infiren.taskmaster.security.jwt;

import com.infiren.taskmaster.security.AccessToken;
import com.infiren.taskmaster.security.IAccessToken;
import com.infiren.taskmaster.security.IRefreshToken;
import com.infiren.taskmaster.security.RefreshToken;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.header.Header;

@AllArgsConstructor
public class JwtAuthenticationConverter implements AuthenticationConverter {

    private final IAccessToken<String, AccessToken> accessTokenStringDeserializer;

    private final IRefreshToken<String, RefreshToken> refreshTokenStringDeserializer;

    @Override
    public @Nullable Authentication convert(HttpServletRequest request) {
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorization != null && authorization.startsWith("Bearer ")) {
            String token = authorization.replace("Bearer ", "");
            AccessToken accessToken = this.accessTokenStringDeserializer.apply(token);
            if (accessToken != null) {
                return new PreAuthenticatedAuthenticationToken(accessToken, token);
            }

            RefreshToken refreshToken = this.refreshTokenStringDeserializer.apply(token);
            if (refreshToken != null) {
                return new PreAuthenticatedAuthenticationToken(refreshToken, token);
            }
        }
        return null;
    }
}
