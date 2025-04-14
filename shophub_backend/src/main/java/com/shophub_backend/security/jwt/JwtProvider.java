package com.shophub_backend.security.jwt;

import java.util.Date;
import javax.crypto.SecretKey;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtProvider {
    private SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());
    private static final long EXPIRATION_TIME = 30*60*100000;

    public String generateToken(Authentication authentication) {
        String role = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority->{
                    return GrantedAuthority.getAuthority();
                })
                .findFirst()
                .orElse("USER");

        return Jwts.builder()
                .subject(authentication.getName())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .claim("email", authentication.getName())
                .claim("role", role)
                .signWith(key)
                .compact();
    }

    public String getEmailFromToken(String token){
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        Claims claim = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
        return (String)claim.get("email");
    }
}
