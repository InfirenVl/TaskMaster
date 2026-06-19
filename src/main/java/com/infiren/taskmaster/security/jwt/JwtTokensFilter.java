package com.infiren.taskmaster.security.jwt;

import com.infiren.taskmaster.security.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import tools.jackson.databind.ObjectMapper;
import java.io.IOException;

@Setter
public class JwtTokensFilter extends OncePerRequestFilter {

    private RequestMatcher requestMatcher = PathPatternRequestMatcher.pathPattern(HttpMethod.POST, "/jwt/tokens");
    //= new AntPathRequestMatcher(, HttpMethod.POST.name());

    private SecurityContextRepository securityContextRepository = new RequestAttributeSecurityContextRepository();

    private IRefreshToken<Authentication, RefreshToken> refreshTokenFactory = new DefaultRefreshTokenFactory();

    private IAccessToken<RefreshToken, AccessToken> accessTokenFactory = new DefaultAccessTokenFactory();

    private IRefreshToken<RefreshToken, String> refreshTokenSerializer = Object::toString;

    private IAccessToken<AccessToken, String> accessTokenSerializer = Object::toString;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (this.requestMatcher.matches(request)) {
            if (this.securityContextRepository.containsContext(request)) {
                SecurityContext context = this.securityContextRepository.loadDeferredContext(request).get();
                if (context != null && !(context.getAuthentication() instanceof PreAuthenticatedAuthenticationToken)) {
                    RefreshToken refreshToken = this.refreshTokenFactory.apply(context.getAuthentication());
                    AccessToken accessToken = this.accessTokenFactory.apply(refreshToken);

                    response.setStatus(HttpServletResponse.SC_OK);
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    this.objectMapper.writeValue(response.getWriter(),
                            new Tokens(this.accessTokenSerializer.apply(accessToken),
                                    accessToken.expiresAt().toString(),
                                    this.refreshTokenSerializer.apply(refreshToken),
                                    refreshToken.expiresAt().toString()));
                    return;
                }
            }
            throw new AccessDeniedException("User must be authenticated");
        }

        filterChain.doFilter(request, response);
    }
}
