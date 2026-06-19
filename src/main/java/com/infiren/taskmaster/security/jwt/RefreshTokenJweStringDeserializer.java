package com.infiren.taskmaster.security.jwt;

import com.infiren.taskmaster.security.IRefreshToken;
import com.infiren.taskmaster.security.RefreshToken;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEDecrypter;
import com.nimbusds.jwt.EncryptedJWT;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.text.ParseException;
import java.util.UUID;

@RequiredArgsConstructor
public class RefreshTokenJweStringDeserializer implements IRefreshToken<String, RefreshToken> {

    private final Logger logger = LoggerFactory.getLogger(RefreshTokenJweStringDeserializer.class);

    private final JWEDecrypter jweDecrypter;

    @Override
    public RefreshToken apply(String s) {
        try{
            EncryptedJWT encryptedJWT = EncryptedJWT.parse(s);
            encryptedJWT.decrypt(this.jweDecrypter);
            JWTClaimsSet jwtClaimsSet = encryptedJWT.getJWTClaimsSet();
                return new RefreshToken(UUID.fromString(
                        jwtClaimsSet.getJWTID()),
                        jwtClaimsSet.getSubject(),
                        jwtClaimsSet.getStringListClaim("authorities"),
                        jwtClaimsSet.getIssueTime().toInstant(),
                        jwtClaimsSet.getExpirationTime().toInstant());
        }catch (ParseException | JOSEException exception){
            logger.error(exception.toString(), exception);
        }
        return null;
    }
}
