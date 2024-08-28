package com.common.solstar.global.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    @Value("${jwt.secret.key}")
    private String secretKey;

    @Value("${jwt.access.expiration}")
    private long accessTokenExpiration;

    @Value("${jwt.refresh.expiration}")
    private long refreshTokenExpiration;

    private SecretKey getSecretKey() {
        byte[] keyBytes = Decoders.BASE64.decode(this.secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Access Token 발급
    public String generateAccessToken(String email, String role) {
        return generateToken(email, accessTokenExpiration, "access_token", role);
    }

    // Refresh Token 발급
    public String generateRefreshToken(String email, String role) {
        return generateToken(email, refreshTokenExpiration, "refresh_token", role);
    }

    // 토큰 생성 메서드
    private String generateToken(String email, long expiration, String type, String role) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(this.getSecretKey())
                .claim("type", type)
                .claim("role", role)
                .compact();
    }

    // 토큰으로부터 이메일 추출
    public String getEmailFromToken(String token) throws ExpiredJwtException, JwtException {
        return getClaimsFromToken(token).getSubject();
    }

    // 토큰으로부터 role 추출
    public String roleFromToken(String token) throws ExpiredJwtException, JwtException {
        return getClaimsFromToken(token).get("role", String.class);
    }

    // AccessToken인지 검사
    public boolean isAccessToken(String token) {
        return "access_token".equals(getClaimsFromToken(token).get("type"));
    }

    // 토큰 유효성 검사
    public boolean validateToken(String token) {
        try {
            getClaimsFromToken(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 토큰으로부터 클레임 추출
    private Claims getClaimsFromToken(String token) throws ExpiredJwtException, JwtException {
        return Jwts.parserBuilder()
                .setSigningKey(this.getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // 토큰의 만료시간 가져오기
    public Date getExpirationFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(this.getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }

}
