package kroryi.spring.controller;

import kroryi.spring.dto.MemberJoinDTO;
import kroryi.spring.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

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
}
