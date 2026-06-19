package com.infiren.taskmaster.security.jwt;

import com.infiren.taskmaster.security.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Objects;

@AllArgsConstructor
@RequiredArgsConstructor
public class RefreshTokenFilter extends OncePerRequestFilter {

    private RequestMatcher requestMatcher = PathPatternRequestMatcher.pathPattern(HttpMethod.POST, "/jwt/refresh");

    private SecurityContextRepository securityContextRepository = new RequestAttributeSecurityContextRepository();

    private IAccessToken<RefreshToken, AccessToken> accessTokenFactory = new DefaultAccessTokenFactory();

    @Setter
    private IAccessToken<AccessToken, String> accessTokenStringSerializer = Objects::toString;

    private ObjectMapper objectMapper = new ObjectMapper();
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(this.requestMatcher.matches(request)){
            if(this.securityContextRepository.containsContext(request)) {
                SecurityContext context = this.securityContextRepository.loadDeferredContext(request).get();
                if (context != null && context.getAuthentication() instanceof PreAuthenticatedAuthenticationToken &&
                context.getAuthentication().getPrincipal() instanceof TokenUser user &&
                        user.getToken() instanceof RefreshToken refreshToken &&
                context.getAuthentication().getAuthorities()
                        .contains(new SimpleGrantedAuthority("JWT_LOGOUT"))) {
                    AccessToken accessToken = this.accessTokenFactory.apply(refreshToken);

                    response.setStatus(HttpServletResponse.SC_OK);
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    this.objectMapper.writeValue(response.getWriter(),
                            new Tokens(this.accessTokenStringSerializer.apply(accessToken),
                                    accessToken.expiresAt().toString(), null, null));
                    return;
                }
            }
            throw new AccessDeniedException("User must be Authenticated with JWT");
        }
        filterChain.doFilter(request, response);
    }

}
