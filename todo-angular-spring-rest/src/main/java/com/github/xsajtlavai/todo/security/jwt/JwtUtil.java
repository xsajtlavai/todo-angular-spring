package com.github.xsajtlavai.todo.security.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public final class JwtUtil {

    public static final long EXPIRATION_TIME_IN_MILLIS = TimeUnit.MINUTES.toMillis(30);
    public static final String SECRET = "SECRET";
    public static final String TOKEN_PREFIX = "Bearer ";

    private JwtUtil() {
    }

    public static String createToken(String subject) {
        return Jwts.builder()
                .setSubject(subject)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_IN_MILLIS))
                .signWith(SignatureAlgorithm.HS512, SECRET.getBytes())
                .compact();
    }

    public static String createTokenWithHeaderPrefix(String subject) {
        return TOKEN_PREFIX + createToken(subject);
    }

    public static String parseSubjectFromToken(String token) {
        String normalizedToken = removeHeaderPrefixFromToken(token);

        return Jwts.parser()
                .setSigningKey(SECRET.getBytes())
                .parseClaimsJws(normalizedToken)
                .getBody()
                .getSubject();
    }

    private static String removeHeaderPrefixFromToken(String token) {
        return token.startsWith(TOKEN_PREFIX) ? token.replace(TOKEN_PREFIX, "") : token;
    }

    public static boolean isTokenHeaderValue(String tokenHeader) {
        return tokenHeader != null && tokenHeader.startsWith(TOKEN_PREFIX);
    }
}
