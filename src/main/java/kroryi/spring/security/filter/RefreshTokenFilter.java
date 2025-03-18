package kroryi.spring.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kroryi.spring.security.JWTUtil;
import kroryi.spring.security.exception.AccessTokenException;
import kroryi.spring.security.exception.RefreshTokenException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Log4j2
@RequiredArgsConstructor
//OncePerRequestFilter는 한번 실행을 보장하는 클래스
public class RefreshTokenFilter extends OncePerRequestFilter {

    private final String refreshPath;
    private final JWTUtil jwtUtil;


    private static final List<String> PUBLIC_URLS = List.of(
            "/swagger-ui", "/swagger-ui.html", "/api/api-docs", "/api/public"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        String path = request.getRequestURI();
        if (!path.startsWith(refreshPath)) {
            log.info("skip refresh token filter ......");
            filterChain.doFilter(request, response);
            return;
        }

        if (PUBLIC_URLS.stream().anyMatch(path::startsWith)) {
            filterChain.doFilter(request, response);
            return;
        }

        Map<String, Object> tokens = parseRequestJSON(request);

        String accessToken = tokens.get("accessToken").toString();
        String refreshToken = tokens.get("refreshToken").toString();

        log.info("======== accessToken: {}", accessToken);
        log.info("======== refreshToken: {}", refreshToken);

        // AccessToken 검사
        try {
            checkAccessToken(accessToken);
        } catch (RefreshTokenException e) {
            e.sendResponseError(response);
        }

        // RefreshToken 검사
        Map<String, Object> refreshClaims = null;

        try {
//            {
//                "payloads": {
//                    "mid": "member1"
//                },
//                "iat": 1742277703,
//                "exp": 1742364103
//            }
            refreshClaims = checkRefreshToken(refreshToken);
            log.info("======== refreshClaims: {}", refreshClaims.get("payloads"));

            Long exp = (Long) refreshClaims.get("exp");
            Date expTime = new Date(Instant.ofEpochMilli(exp).toEpochMilli() * 1000);

            Date currentTime = new Date(System.currentTimeMillis());

            long gapTime = (expTime.getTime() - currentTime.getTime());
            log.info("======== currentTime: {}", currentTime);
            log.info("======== expTime: {}", expTime);
            log.info("======== gap : {}", gapTime);
            Map<String, Object> cliam= (Map<String, Object>) refreshClaims.get("payloads");

            String accessTokenValue = jwtUtil.generateToken(Map.of(
                    "mid", cliam.get("mid")
            ), 1);

            String refreshTokenValue = tokens.get("refreshToken").toString();

            if (gapTime < (1000 * 60 * 60 * 24 * 3)) { // 3일
                log.info("새로 refresh발급 요구");
                refreshTokenValue = jwtUtil.generateToken(Map.of("mid",  cliam.get("mid")), 2);
            }
            log.info("========= accessTokenValue: {}", accessTokenValue);
            log.info("======== refreshTokenValue: {}", refreshTokenValue);

            // response를 위 전달하나?
            sendTokens(accessTokenValue, refreshTokenValue, response);

        } catch (RefreshTokenException e) {
            e.sendResponseError(response);
        }

        return;
    }

    private void sendTokens(String accessToken,
                            String refreshToken,
                            HttpServletResponse resp) throws IOException {
        resp.setContentType(MediaType.APPLICATION_JSON_VALUE);

        // Map to Json
        Map<String, Object> keyMap = Map.of("accessToken", accessToken,
                "refreshToken", refreshToken);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonStr = objectMapper.writeValueAsString(keyMap);

        log.info("jsonStr 값: {}", jsonStr);
        // 응답
        resp.getWriter().println(jsonStr);
    }


    private Map<String, Object> parseRequestJSON(HttpServletRequest request) {
        try (BufferedReader reader = request.getReader()) { // BufferedReader 사용
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(reader, Map.class); // JSON을 Map으로 변환
        } catch (IOException e) {
            throw new RuntimeException("Error parsing JSON request", e);
        }
    }

    private void checkAccessToken(String token) throws RefreshTokenException {

        try {
            jwtUtil.validateToken(token);
        } catch (ExpiredJwtException e) {
            log.info("Access Token has expired");
        } catch (Exception e) {
            throw new RefreshTokenException(RefreshTokenException.ErrorCase.NO_ACCESS);
        }
    }

    private Map<String, Object> checkRefreshToken(String token) throws RefreshTokenException {

        try {
            Map<String, Object> values = jwtUtil.validateToken(token);
            return values;
        } catch (ExpiredJwtException e) {
            throw new RefreshTokenException(RefreshTokenException.ErrorCase.OLD_REFRESH);
        } catch (MalformedJwtException e) {
            throw new RefreshTokenException(RefreshTokenException.ErrorCase.NO_REFRESH);
        } catch (Exception e) {
            new RefreshTokenException(RefreshTokenException.ErrorCase.NO_REFRESH);
        }

        return null;
    }


}
