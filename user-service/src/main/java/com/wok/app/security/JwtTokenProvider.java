package com.wok.app.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class JwtTokenProvider {

    @Value("${jwt.secret:your-secret-key-minimum-32-characters-for-hs256-algorithm}")
    private String jwtSecret;

    @Value("${jwt.expiration:86400000}")
    private long jwtExpirationMs;

    public String generateToken(String userId, String email) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
            .setSubject(userId)
            .claim("email", email)
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(SignatureAlgorithm.HS512, jwtSecret.getBytes())
            .compact();
    }

    public String getUserIdFromToken(String token) {
        try {
            return Jwts.parser()
                .setSigningKey(jwtSecret.getBytes())
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        } catch (Exception e) {
            log.error("Error extracting user ID from token", e);
            return null;
        }
    }

    public String getEmailFromToken(String token) {
        try {
            return (String) Jwts.parser()
                .setSigningKey(jwtSecret.getBytes())
                .parseClaimsJws(token)
                .getBody()
                .get("email");
        } catch (Exception e) {
            log.error("Error extracting email from token", e);
            return null;
        }
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                .setSigningKey(jwtSecret.getBytes())
                .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            log.error("JWT validation failed: {}", e.getMessage());
            return false;
        }
    }
}
