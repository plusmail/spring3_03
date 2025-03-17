package kroryi.spring.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

@Log4j2
public class APILoginFilter extends AbstractAuthenticationProcessingFilter {

    public APILoginFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
    }

    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request,
            HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {

        log.info("----- APILoginFilter attemptAuthentication");
        if(request.getMethod().equalsIgnoreCase("GET")){
            log.info("----- APILoginFilter GET METHOOD NOT SUPPORTED");
            return null;
        }

        Map<String,Object> jsonData = parseRequestJSON(request);
        // html에서 js로 mid,mpw를 post방식으로 axios전송 jsonData = {mid:"member1", mpw:"1111"}

        log.info("----- APILoginFilter json: {}", jsonData);

        // 로그인 진행
        UsernamePasswordAuthenticationToken authRequest =
                new UsernamePasswordAuthenticationToken(
                jsonData.get("mid"),
                jsonData.get("mpw")
        );

        return getAuthenticationManager().authenticate(authRequest);
    }

    private Map<String,Object> parseRequestJSON(HttpServletRequest request) {
        try (BufferedReader reader = request.getReader()) { // BufferedReader 사용
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(reader, Map.class); // JSON을 Map으로 변환
        } catch (IOException e) {
            throw new RuntimeException("Error parsing JSON request", e);
        }
    }
}
