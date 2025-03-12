package kroryi.spring.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Log4j2
@RequiredArgsConstructor
public class LoginController {


    @GetMapping("/login")
    public String login(String errorCode, String logout) {
        log.info("login.............");
        log.info("logout: {}", logout);
        if(logout != null) {
            log.info("사용자 로그아웃...");
        }

        return "login";
    }
}
