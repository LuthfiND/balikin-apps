package org.balikin.util;

import io.smallrye.jwt.auth.principal.*;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import org.jose4j.jwt.JwtClaims;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@ApplicationScoped
@Alternative
@Priority(1)
public class CustomJWTCallerPrincipalFactory extends JWTCallerPrincipalFactory {
    @Override
    public JWTCallerPrincipal parse(String token, JWTAuthContextInfo jwtAuthContextInfo) throws ParseException {
        try{
            String  json  =  new  String (Base64.getUrlDecoder().decode(token.split( "\\." )[ 1 ]), StandardCharsets.UTF_8);
            return new DefaultJWTCallerPrincipal(JwtClaims.parse(json));
        } catch (Exception e) {
            throw  new  ParseException (e.getMessage());
        }

    }
}
