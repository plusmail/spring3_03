package kroryi.spring.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kroryi.spring.dto.MemberSecurityDTO;
import kroryi.spring.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Repository;

import java.io.IOException;

@Log4j2
@RequiredArgsConstructor
public class CustomSocialLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final PasswordEncoder passwordEncoder;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {
        log.info("------------- 소셜 로그인 성공 처리 -----------");
        log.info(authentication.getPrincipal());
        MemberSecurityDTO memberSecurityDTO = (MemberSecurityDTO) authentication.getPrincipal();
        String encodedPw = memberSecurityDTO.getMpw();

        if (
                memberSecurityDTO.isSocial()
                        && (memberSecurityDTO.getMpw().equals("1111")
                        || passwordEncoder.matches("1111", memberSecurityDTO.getMpw()))
        ) {
            log.info("비밀번호 변경.....으로 이동");
            response.sendRedirect("/member/modify");
            return;
        }else{
            response.sendRedirect("/");
        }

    }
}
