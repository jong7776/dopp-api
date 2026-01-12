package com.dopp.doppapi.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private final String secret = "mySuperSecretKey12345mySuperSecretKey12345";
    private final long accessTokenValidity = 15 * 60 * 1000; // 15분
    private final long refreshTokenValidity = 7 * 24 * 60 * 60 * 1000; // 7일

    private Key getSigningKey() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Access Token 생성
    public String generateAccessToken(String loginId, String role) {
        return createToken(loginId, role, accessTokenValidity);
    }

    // Refresh Token 생성
    public String generateRefreshToken(String loginId) {
        return createToken(loginId, null, refreshTokenValidity);
    }

    private String createToken(String loginId, String role, long validity) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + validity);

        var builder = Jwts.builder()
                .setSubject(loginId)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256);

        if (role != null) {
            builder.claim("role", role);
        }

        return builder.compact();
    }

    // 토큰에서 loginId 추출
    public String getLoginIdFromToken(String token) {
        return parseClaims(token).getSubject();
    }

    // 토큰 검증
    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
