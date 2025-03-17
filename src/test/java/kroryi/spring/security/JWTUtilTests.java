package kroryi.spring.security;

import io.jsonwebtoken.Claims;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

@SpringBootTest
@Log4j2
public class JWTUtilTests {

    @Autowired
    private JWTUtil jwtUtil;

    @Test
    public void testGenerateToken() {
        //payloads
        Map<String,Object> claimMap = Map.of(
                "mid","abcdef",
                "mpw","1111",
                "email","aaa@a.co.kr"
        );
        String jswStr = jwtUtil.generateToken(claimMap,1);
        log.info(jswStr);
    }

    @Test
    public void testVerifyToken() {
        String jwtStr = "eyLsgqzsmqnsnpAt7Zek65OcIjoi7JuQ7ZWY64qUIOqwkiDrhKPquLAiLCJhbGciOiJIUzI1NiJ9.eyJwYXlsb2FkcyI6eyJtaWQiOiJhYmNkZWYiLCJtcHciOiIxMTExIiwiZW1haWwiOiJhYWFAYS5jby5rciJ9LCJpYXQiOjE3NDIxOTMwOTcsImV4cCI6MTc0MjI3OTQ5N30.UL-kH80Y8hmU8xqSDB_bAOQaF5nF3WYcbY-Lf2gOObE";

//         {payloads={mid=abcdef, mpw=1111, email=aaa@a.co.kr}, iat=1742193097, exp=1742279497}
//        payloads 도 맵 그 안쪽에 mid, mpw, email  ,iat  , exp

        Map<String,Object> claim= jwtUtil.validateToken(jwtStr);

        Map<String,Object> payloads = (Map<String, Object>) claim.get("payloads");

        log.info("test-----1 {}", payloads.get("mid"));
        log.info("test-----1 {}", payloads.get("mpw"));
        log.info("test-----1 {}", payloads.get("email"));
    }
}
