package com.infiren.taskmaster.security.jwt;

import com.infiren.taskmaster.security.IRefreshToken;
import com.infiren.taskmaster.security.RefreshToken;
import com.nimbusds.jose.*;
import com.nimbusds.jwt.EncryptedJWT;
import com.nimbusds.jwt.JWTClaimsSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Date;

public class RefreshTokenJweStringSerializer implements IRefreshToken<RefreshToken, String> {

    private static final Logger log = LoggerFactory.getLogger(RefreshTokenJweStringSerializer.class);

    private final JWEEncrypter jweEncrypter;

    private JWEAlgorithm jweAlgorithm = JWEAlgorithm.DIR;

    private EncryptionMethod encryptionMethod = EncryptionMethod.A128GCM;

    public RefreshTokenJweStringSerializer(JWEEncrypter jweEncrypter) {
        this.jweEncrypter = jweEncrypter;
    }

    public RefreshTokenJweStringSerializer(JWEEncrypter jweEncrypter, JWEAlgorithm jweAlgorithm, EncryptionMethod encryptionMethod) {
        this.jweEncrypter = jweEncrypter;
        this.jweAlgorithm = jweAlgorithm;
        this.encryptionMethod = encryptionMethod;
    }

    @Override
    public String apply(RefreshToken refreshToken) {
        JWEHeader jweHeader = new JWEHeader.Builder(this.jweAlgorithm, this.encryptionMethod)
                .keyID(refreshToken.id().toString())
                .build();

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .jwtID(refreshToken.id().toString())
                .subject(refreshToken.subject())
                .issueTime(Date.from(refreshToken.createdAt()))
                .expirationTime(java.util.Date.from(refreshToken.expiresAt()))
                .claim("authorities", refreshToken.authorities())
                .build();

        EncryptedJWT encryptedJWT = new EncryptedJWT(jweHeader, claimsSet);

        try {
            encryptedJWT.encrypt(this.jweEncrypter);

            return encryptedJWT.serialize();
        } catch (JOSEException e) {
            log.error(e.getMessage(), e);
        }

        //TODO
        return null;
    }
}
