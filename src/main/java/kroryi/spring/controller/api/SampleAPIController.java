package kroryi.spring.controller.api;

import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@RestController
@RequestMapping("/api")
public class SampleAPIController {

    @ApiOperation("셈플 GET doA")
    @GetMapping("/sample/doA")
    public List<String> doA() {
        return Arrays.asList("AAAA", "BBBB", "CCCC");
    }


    @GetMapping("/public")
    public String publicEndpoint() {
        return "이 메서드는 public API";
    }

    @SecurityRequirement(name = "BearerAuth")
    @GetMapping("/secure")
    public String secureGetEndpoint(@RequestParam String name,
                                    @RequestParam String age,
                                    @RequestParam String email,
                                    @RequestHeader("Authorization") String authorizationHeader) {

        log.info("이름Get : {}", name);
        log.info("나이Get : {}", age);
        log.info("메일Get : {}", email);
        log.info("토큰Get : {}", authorizationHeader);


        return "이 메소드는 보안용 Get API";
    }


    @SecurityRequirement(name = "BearerAuth")
    @PostMapping("/secure")
    public ResponseEntity<Map<String,Object>> secureEndpoint(@RequestBody Map<String, Object> requestBody,
                                         @RequestHeader("Authorization") String authorizationHeader) {

        log.info("Data : {}", requestBody);
        log.info("토큰 : {}", authorizationHeader);
        Map<String, Object> map = new HashMap<>();

        if(requestBody.get("name").equals("홍길동")){
            map.put("status", 200);
            map.put("msg", "성공적으로 데이터 전송");
            map.put("data", requestBody);
        }else if(requestBody.get("name").equals("김유신")){
            map.put("status", 300);
            map.put("msg", "데이터 확인 좀해");
            map.put("data", requestBody);
        }else{
            map.put("status", 500);
            map.put("msg", "오류발생");
            map.put("data", requestBody);
        }
        return ResponseEntity.ok(map);
    }
}
