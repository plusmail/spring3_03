package kroryi.spring.security.exception;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import org.springframework.http.MediaType;

import java.util.Date;
import java.util.Map;

public class AccessTokenException extends RuntimeException {

    TOKEN_ERROR token_error;

    @Getter
    public enum TOKEN_ERROR {
        UNACCEPT(401,"토큰이 null 이거나 너무 짧습니다."),
        BADTYPE(401,"토큰형태 Bearer"),
        MALFORM(403, "Malformed Token"),
        BADSIGN(403, "BadSignatured Toke"),
        EXPIRED(403,"Expired Token");

        private final int status;
        private final String msg;
        TOKEN_ERROR(int status, String msg) {
            this.status = status;
            this.msg = msg;
        }

    }

    public AccessTokenException(TOKEN_ERROR token_error) {
        super(token_error.getMsg());
        this.token_error = token_error;
    }

    public void sendResponseError(HttpServletResponse resp) throws JsonProcessingException {
        resp.setStatus(token_error.getStatus());
        resp.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Map<String, Object> map = Map.of(
                "msg", token_error.getMsg(),
                "time", new Date()
        );

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonStr = objectMapper.writeValueAsString(map);

        try{
            resp.getWriter().println(jsonStr);
        }catch(Exception e){
            throw new RuntimeException(e);
        }

    }


}
