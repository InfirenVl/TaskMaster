package com.infiren.taskmaster.security.jwt;

import com.infiren.taskmaster.security.RefreshToken;
import com.infiren.taskmaster.security.TokenUser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.sql.Date;

@AllArgsConstructor
@RequiredArgsConstructor
public class JwtLogoutFilter extends OncePerRequestFilter {

    private RequestMatcher requestMatcher = PathPatternRequestMatcher.pathPattern(HttpMethod.POST, "/jwt/logout");

    private SecurityContextRepository securityContextRepository = new RequestAttributeSecurityContextRepository();

    private final JdbcTemplate jdbcTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(this.requestMatcher.matches(request)) {
            if (this.securityContextRepository.containsContext(request)) {
                SecurityContext context = this.securityContextRepository.loadDeferredContext(request).get();
                if (context != null && context.getAuthentication() instanceof PreAuthenticatedAuthenticationToken &&
                        context.getAuthentication().getPrincipal() instanceof TokenUser user &&
                        context.getAuthentication().getAuthorities()
                                .contains(new SimpleGrantedAuthority("JWT_REFRESH"))) {
                    this.jdbcTemplate.update("insert into deactivated_tokens(id, keep_until) values (?, ?)",
                            user.getToken().id(), Date.from(user.getToken().expiresAt()));

                    response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                    return;
                }
            }
            throw new AccessDeniedException("User must be Authenticated with JWT");
        }
        filterChain.doFilter(request, response);
    }
}
