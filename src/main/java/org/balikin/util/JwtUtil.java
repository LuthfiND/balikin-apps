package org.balikin.util;

import io.smallrye.jwt.auth.principal.JWTAuthContextInfo;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.InternalServerErrorException;
import lombok.extern.slf4j.Slf4j;
import java.time.Duration;
@Slf4j
@ApplicationScoped
public class JwtUtil {
    public String generateJwt (String email, String username , Integer userId) throws Exception {
        try {
            return Jwt.issuer("https://your-issuer.com")
                    .subject(userId.toString())
                    .groups("USER")
                    .claim("username", username)
                    .claim("email", email)
                    .expiresIn(Duration.ofHours(1))
                    .sign();
        } catch (Exception e) {
            throw  new InternalServerErrorException(e.getMessage());
        }
    }
    public void verifyJwt (String token) throws Exception {
        try {
            CustomJWTCallerPrincipalFactory factory = new CustomJWTCallerPrincipalFactory();
            JWTAuthContextInfo jwtAuthContext = new JWTAuthContextInfo();
            jwtAuthContext.setIssuedBy("https://example.com/issuer");
            jwtAuthContext.setPublicKeyLocation("new URI(\"/publicKey.pem\")");
            factory.parse(token,jwtAuthContext);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}

