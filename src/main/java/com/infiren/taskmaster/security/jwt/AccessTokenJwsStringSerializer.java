package com.infiren.taskmaster.security.jwt;

import com.infiren.taskmaster.security.AccessToken;
import com.infiren.taskmaster.security.IAccessToken;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.Date;

public class AccessTokenJwsStringSerializer implements IAccessToken<AccessToken, String> {

    private static final Logger log = LoggerFactory.getLogger(AccessTokenJwsStringSerializer.class);

    private final JWSSigner jwsSigner;

    private JWSAlgorithm jwsAlgorithm = JWSAlgorithm.HS256;

    public AccessTokenJwsStringSerializer(JWSAlgorithm jwsAlgorithm, JWSSigner jwsSigner) {
        this.jwsAlgorithm = jwsAlgorithm;
        this.jwsSigner = jwsSigner;
    }

    public AccessTokenJwsStringSerializer(JWSSigner jwsSigner) {
        this.jwsSigner = jwsSigner;
    }

    @Override
    public String apply(AccessToken accessToken) {
        JWSHeader jwsHeader = new JWSHeader.Builder(this.jwsAlgorithm)
                .keyID(accessToken.id().toString())
                .build();

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .jwtID(accessToken.id().toString())
                .subject(accessToken.subject())
                .issueTime(Date.from(accessToken.createdAt()))
                .expirationTime(java.util.Date.from(accessToken.expiresAt()))
                .claim("authorities", accessToken.authorities())
                .build();

        SignedJWT signedJWT = new SignedJWT(jwsHeader, claimsSet);

        try {
            signedJWT.sign(this.jwsSigner);

            return signedJWT.serialize();
        } catch (JOSEException e) {
            log.error(e.getMessage(), e);
        }

        //TODO
        return null;
    }
}
