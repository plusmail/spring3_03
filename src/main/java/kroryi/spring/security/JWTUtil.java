package kroryi.spring.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.DecodingException;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Log4j2
public class JWTUtil {

    @Value("${kroryi.jwt.secret}")
    private String key;
    private SecretKey signingKey;

    public String generateToken(Map<String, Object> valueMap, int days){
        log.info("----- JWTUtil generateToken -----");
        log.info("key: {}", key);

        // Base64 디코딩을 사용하여 충분한 길이의 키 생성
        byte[] keyBytes = Base64.getDecoder().decode(key);
        SecretKey secretKey = Keys.hmacShaKeyFor(keyBytes); // 안전한 키 생성
        Map<String, Object> payloads = new HashMap<>();
        payloads.putAll(valueMap);


        return Jwts.builder()
                .header().add("사용자-헤드","원하는 값 넣기")
                .and()
                .claim("payloads",payloads)
                .issuedAt(Date.from(ZonedDateTime.now().toInstant()))
                .expiration(Date.from(ZonedDateTime.now().plusDays(days).toInstant()))
                .signWith(secretKey)
                .compact();
    }

    public Map<String, Object> validateToken(String token) {
        this.signingKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(key));

        try {
             Claims claims = (Claims) Jwts.parser().verifyWith(this.signingKey)
                     .build().parseSignedClaims(token).getPayload();
            return claims;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.error("Invalid jwt signature.", e);
            throw e;
        } catch (ExpiredJwtException e) {
            log.error("This token is expired.", e);
            throw e;
        } catch (UnsupportedJwtException e) {
            log.error("This jwt token is not supported.", e);
            throw e;
        } catch (IllegalArgumentException e) {
            log.error("Invalid jwt token.", e);
            throw e;
        } catch (DecodingException e) {
            log.error("JWT token decoding failed", e);
            throw e;
        }

    }
}
