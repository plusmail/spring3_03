package kroryi.spring.security.filter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kroryi.spring.security.JWTUtil;
import kroryi.spring.security.exception.AccessTokenException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Log4j2
@RequiredArgsConstructor
//OncePerRequestFilter는 한번 실행을 보장하는 클래스
public class TokenCheckFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    private static final List<String> PUBLIC_URLS = List.of(
            "/swagger-ui","/swagger-ui.html","/api/api-docs", "/api/public"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        String path = request.getRequestURI();
        if(!path.startsWith("/api/")){
            filterChain.doFilter(request, response);
            return;
        }

        if(PUBLIC_URLS.stream().anyMatch(path::startsWith)){
            filterChain.doFilter(request, response);
            return;
        }

        log.info("토큰 체그 필터......");
        log.info("JWTUltil: {}", jwtUtil);

        try{
            validateAccessToken(request);
            filterChain.doFilter(request, response); // 이 놈은 다음 필터로 넘기는 호출
        }catch(AccessTokenException e){
            e.sendResponseError(response);
        }
    }


    private Map<String, Object> validateAccessToken(HttpServletRequest request){

        String headerStr = request.getHeader("Authorization");

        if(headerStr == null || headerStr.length() < 8){
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.UNACCEPT);
        }

        String tokenType = headerStr.substring(0, 6);
        String tokenStr = headerStr.substring(7);

        if(!tokenType.equalsIgnoreCase("Bearer")){
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.BADTYPE);
        }

        try{
            Map<String,Object> values = jwtUtil.validateToken(tokenStr);
            return values;
        }catch (MalformedJwtException e){
            log.error("MalformedJwtException--------------------");
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.MALFORM);
        }catch (SignatureException e){
            log.error("SignatureException-----------------------");
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.BADSIGN);
        }catch (ExpiredJwtException e){
            log.error("ExpiredJwtException---------------------");
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.EXPIRED);
        }

    }


}
