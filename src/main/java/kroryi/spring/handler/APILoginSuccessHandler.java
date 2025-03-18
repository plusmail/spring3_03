package kroryi.spring.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kroryi.spring.security.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Log4j2
@RequiredArgsConstructor
public class APILoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        log.info("----------- Login success onAuthenticationSuccess handler");

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        log.info("로그인 성공... 핸들러");
        log.info(authentication.getName());
        Map<String, Object> claim = Map.of("mid", authentication.getName());
        String accessToken = jwtUtil.generateToken(claim, 1);

        String refreshToken = jwtUtil.generateToken(claim, 2);


        Map<String, Object> keyMap = Map.of("accessToken", accessToken, "refreshToken", refreshToken);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonStr = objectMapper.writeValueAsString(keyMap);

        log.info("jsonStr 값: {}", jsonStr);

        response.getWriter().println(jsonStr);

    }
}
