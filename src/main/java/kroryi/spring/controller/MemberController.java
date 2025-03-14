package kroryi.spring.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kroryi.spring.dto.MemberJoinDTO;
import kroryi.spring.dto.MemberSecurityDTO;
import kroryi.spring.repository.MemberRepository;
import kroryi.spring.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    private final MemberRepository memberRepository;

    @GetMapping("/join")
    public String joinGet() {
        log.info("-------join get -------");
        return "/member/join";
    }

    @PostMapping("/join")
    public String joinPost(MemberJoinDTO dto, RedirectAttributes redirectAttributes){
        log.info("-------join post -------");
        log.info(dto.toString());

        try{
            memberService.join(dto);
        }catch (Exception e){
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "mid");
            return "redirect:/member/join";
        }

        redirectAttributes.addFlashAttribute("result", "success");

        return "redirect:/login";
    }

    @GetMapping("/modify")
    public String modifyForm(
            @AuthenticationPrincipal MemberSecurityDTO memberDTO,
            Model model) {

        if(memberDTO == null){
            return "redirect:/login";
        }
        model.addAttribute("member", memberDTO);

        return "/member/modify";
    }


    @PostMapping("/modify")
    @ResponseBody
    public ResponseEntity<?> modifyMember(
            @AuthenticationPrincipal MemberSecurityDTO memberDTO,
            @RequestBody Map<String, String> requestBody,
            Model model
    ){

        log.info("-------modify post -------");
        log.info(memberDTO.toString());

        String mid = requestBody.get("mid");
        String currentPassword = requestBody.get("current_mpw");
        String newPassword = requestBody.get("new_mpw");
        String reNewPassword = requestBody.get("re_new_mpw");
        String email = requestBody.get("email");

        if(memberDTO == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "로그인 필요합니다."));
        }

        if(!passwordEncoder.matches(currentPassword, memberDTO.getMpw())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "현재 비밀번호가 일치 하지 않습니다."));
        }

        if(!memberDTO.getEmail().equals(email)){

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Email이 일치 하지 않습니다."));
        }

        if(newPassword.isEmpty() || reNewPassword.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "새로운 비밀번호 칸이 비워져 있습니다."));
        }

        if(!newPassword.equals(reNewPassword)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "새 비밀번호가 일치하지 않습니다."));
        }

        memberDTO.setEmail(email);
        memberDTO.setMpw(passwordEncoder.encode(newPassword));
        try {
            memberService.updateMember(memberDTO);
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "회원 정보가 성공적으로 수정 되었습니다."));
        } catch (MemberService.MidExitException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "아이디에 문제가 있습니다."));
        } catch (MemberService.MpwExitException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "비밀번호에 문제가 있습니다."));
        }

    }

}
