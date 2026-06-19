package com.infiren.taskmaster.security.jwt;

import com.infiren.taskmaster.security.AccessToken;
import com.infiren.taskmaster.security.IAccessToken;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.util.UUID;

@AllArgsConstructor
public class AccessTokenJwsStringDeserializer implements IAccessToken<String, AccessToken> {

    private final Logger logger = LoggerFactory.getLogger(AccessTokenJwsStringDeserializer.class);

    private final JWSVerifier jwsVerifier;

    @Override
    public AccessToken apply(String s) {
        try{
            SignedJWT signedJWT = SignedJWT.parse(s);
            if(signedJWT.verify(this.jwsVerifier)){
                JWTClaimsSet jwtClaimsSet = signedJWT.getJWTClaimsSet();
                return new AccessToken(UUID.fromString(
                        jwtClaimsSet.getJWTID()),
                        jwtClaimsSet.getSubject(),
                        jwtClaimsSet.getStringListClaim("authorities"),
                        jwtClaimsSet.getIssueTime().toInstant(),
                        jwtClaimsSet.getExpirationTime().toInstant());
            }
        }catch (ParseException | JOSEException exception){
            logger.error(exception.toString(), exception);
        }
        return null;
    }
}
