package com.infiren.taskmaster.security.jwt;

import com.infiren.taskmaster.security.*;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;


public class JwtAuthenticationConfigurer extends AbstractHttpConfigurer<JwtAuthenticationConfigurer, HttpSecurity> {

    @Value("${jwt.path-pattern}") String pathPattern;
    private RequestMatcher requestMatcher = PathPatternRequestMatcher.pathPattern(HttpMethod.POST, "/jwt/tokens");

    private SecurityContextRepository securityContextRepository = new RequestAttributeSecurityContextRepository();

    private IRefreshToken<Authentication, RefreshToken> refreshTokenFactory = new DefaultRefreshTokenFactory();

    private IAccessToken<RefreshToken, AccessToken> accessTokenFactory = new DefaultAccessTokenFactory();

    private IRefreshToken<RefreshToken, String> refreshTokenSerializer = Object::toString;

    private IAccessToken<AccessToken, String> accessTokenSerializer = Object::toString;

    private IAccessToken<String, AccessToken> accessTokenStringDeserializer;

    private IRefreshToken<String, RefreshToken> refreshTokenStringDeserializer;

    private JdbcTemplate jdbcTemplate;



    @Override
    public void init(HttpSecurity builder) {
        CsrfConfigurer csrfConfigurer = builder.getConfigurer(CsrfConfigurer.class);

        if(csrfConfigurer != null){
            csrfConfigurer.ignoringRequestMatchers(PathPatternRequestMatcher.pathPattern(HttpMethod.POST, pathPattern));
        }
    }

    @Override
    public void configure(HttpSecurity builder) {
        JwtTokensFilter jwtTokensFilter = new JwtTokensFilter();

        jwtTokensFilter.setRequestMatcher(this.requestMatcher);
        jwtTokensFilter.setSecurityContextRepository(this.securityContextRepository);
        jwtTokensFilter.setRefreshTokenFactory(this.refreshTokenFactory);
        jwtTokensFilter.setAccessTokenFactory(this.accessTokenFactory);
        jwtTokensFilter.setAccessTokenSerializer(this.accessTokenSerializer);
        jwtTokensFilter.setRefreshTokenSerializer(this.refreshTokenSerializer);

        AuthenticationFilter jwtAuthenticationFilter = new AuthenticationFilter(builder.getSharedObject(AuthenticationManager.class),
                new JwtAuthenticationConverter(this.accessTokenStringDeserializer, this.refreshTokenStringDeserializer));

        jwtAuthenticationFilter.setSuccessHandler((request, response, authentication) -> {
            CsrfFilter.skipRequest(request);
        });

        jwtAuthenticationFilter.setFailureHandler((request, response, exception)-> {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN);
        });

        PreAuthenticatedAuthenticationProvider authenticationProvider = new PreAuthenticatedAuthenticationProvider();

        authenticationProvider.setPreAuthenticatedUserDetailsService(new TokenAuthenticationUserDetailsService(this.jdbcTemplate));

        RefreshTokenFilter refreshTokenFilter = new RefreshTokenFilter();
        refreshTokenFilter.setAccessTokenStringSerializer(this.accessTokenSerializer);

        JwtLogoutFilter jwtLogoutFilter = new JwtLogoutFilter(this.jdbcTemplate);

        builder.addFilterAfter(jwtTokensFilter, ExceptionTranslationFilter.class)
                .addFilterBefore(jwtAuthenticationFilter, CsrfFilter.class)
                .addFilterAfter(refreshTokenFilter, ExceptionTranslationFilter.class)
                .addFilterAfter(jwtLogoutFilter, ExceptionTranslationFilter.class)
                .authenticationProvider(authenticationProvider);
    }

    public JwtAuthenticationConfigurer requestMatcher(RequestMatcher requestMatcher) {
        this.requestMatcher = requestMatcher;
        return this;
    }

    public JwtAuthenticationConfigurer securityContextRepository(
            SecurityContextRepository securityContextRepository) {
        this.securityContextRepository = securityContextRepository;
        return this;
    }

    public JwtAuthenticationConfigurer refreshTokenFactory(
            IRefreshToken<Authentication, RefreshToken> refreshTokenFactory) {
        this.refreshTokenFactory = refreshTokenFactory;
        return this;
    }

    public JwtAuthenticationConfigurer refreshTokenSerializer(
            IRefreshToken<RefreshToken, String> refreshTokenSerializer) {
        this.refreshTokenSerializer = refreshTokenSerializer;
        return this;
    }

    public JwtAuthenticationConfigurer accessTokenFactory(
            IAccessToken<RefreshToken, AccessToken> accessTokenFactory) {
        this.accessTokenFactory = accessTokenFactory;
        return this;
    }

    public JwtAuthenticationConfigurer accessTokenSerializer(
            IAccessToken<AccessToken, String> accessTokenSerializer) {
        this.accessTokenSerializer = accessTokenSerializer;
        return this;
    }

    public JwtAuthenticationConfigurer refreshTokenStringDeserializer(IRefreshToken<String, RefreshToken> refreshTokenStringDeserializer) {
        this.refreshTokenStringDeserializer = refreshTokenStringDeserializer;
        return this;
    }

    public JwtAuthenticationConfigurer accessTokenStringDeserializer(IAccessToken<String, AccessToken> accessTokenStringDeserializer) {
        this.accessTokenStringDeserializer = accessTokenStringDeserializer;
        return this;
    }

    public JwtAuthenticationConfigurer jdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        return this;
    }
}
