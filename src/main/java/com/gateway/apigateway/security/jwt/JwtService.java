package com.gateway.apigateway.security.jwt;

import com.gateway.apigateway.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@Service
public class JwtService {

    private static final String SECRET_KEY = "6D5971337436763979244226452948404D635166546A576E5A7234753778217A25432A462D4A614E645267556B58703273357638792F423F4428472B4B625065";

    private static final int JWT_VALIDITY = 60 * 60 * 5 * 1000;

    private static final String CLAIM_ROLE = "role";

    private static final String CLAIM_ID = "id";


    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(final String token) {
        return extractClaim(token, Claims::getSubject).replace("\n", "");
    }

    public Long extractId(final String token) {
        Claims claims = Jwts.parser()
                        .setSigningKey(SECRET_KEY)
                        .parseClaimsJws(token)
                        .getBody();
        return Long.valueOf(((Integer) claims.get("id")).longValue());
    }

    public Date extractExpiration(final String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String generateToken(User user) {
        return generateToken(new HashMap<>(), user);
    }

    public String generateToken(Map<String, Object> extraClaims, User user) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .claim(CLAIM_ROLE, user.getRole().name())
                .claim(CLAIM_ID, user.getId())
                .setSubject(String.format("%s", user.getUsername()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_VALIDITY))
                .signWith(getSignInKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public boolean isTokenValid(String token, User user) {
        final Long id = extractId(token);
        final String email = extractUsername(token);
        return Objects.equals(id, user.getId()) && email.equals(user.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(final String token) {
        return extractExpiration(token).before(new Date());
    }
}
