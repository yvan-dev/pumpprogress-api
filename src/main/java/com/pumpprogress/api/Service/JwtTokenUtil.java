package com.pumpprogress.api.Service;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

import com.pumpprogress.api.Model.Role;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtTokenUtil implements Serializable {

    private static final int JWT_TOKEN_VALIDITY = 1 * 15 * 60; // hh * mm * ss
    private static final String SECRET_KEY = "seyJhbGciOiJIUzUxMiJ9.eyJhdXRob3JpdGllc";
    private static final SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

    public String getUsernameFromToken(String jwtToken) {
        System.out.println("Authorities: " + Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(jwtToken)
                .getPayload()
                .get("authorities"));
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(jwtToken)
                .getPayload()
                .getSubject();
    }

    public String generateToken(String email, Set<Role> roles) throws NoSuchAlgorithmException {
        Map<String, Object> claims = new HashMap<>();
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList(roles.stream()
                        .map(Role::getName)
                        .collect(Collectors.joining(",")));

        claims.put("authorities",
                grantedAuthorities.stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()));
        claims.put("timestamp", System.currentTimeMillis());

        return Jwts.builder()
        .claims().empty().add(claims).and()
                .subject(email)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(key)
                .compact();
    }

    public boolean validateToken(String jwtToken, String email) {
        final String username = getUsernameFromToken(jwtToken);
        return (username.equals(email) && !isTokenExpired(jwtToken));
    }

    private boolean isTokenExpired(String jwtToken) {
        final Date expiration = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(jwtToken)
                .getPayload()
                .getExpiration();
        return expiration.before(new Date());
    }

}
