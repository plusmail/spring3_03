package kroryi.spring.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.LocaleResolver;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@Log4j2

public class SampleController {

    @Value("${server.port}")
    private String serverPort;

    @Value("${test.price}")
    private String testPrice;

    private final MessageSource messageSource;
    private final LocaleResolver localeResolver;

    public SampleController(MessageSource messageSource, LocaleResolver localeResolver) {
        this.messageSource = messageSource;
        this.localeResolver = localeResolver;
    }

    @GetMapping("/hello")
    public String hello(Model model) {
        log.info("hello..............");
        model.addAttribute("message", "반가워요.. boot..님");

        return "hello";
    }


    @GetMapping("/ex/ex1")
    public String ex1(Model model) {
        List<String> list = Arrays.asList("가가가가","나나나나", "다다다다다", "마마마마");
        model.addAttribute("list", list);
        model.addAttribute("username", "홍길동");
        model.addAttribute("age", 30);

        return "hello";
    }

    class SampleDTO {
        private String p1,p2,p3;

        public String getP1() {
            return p1;
        }
        public String getP2() {
            return p2;
        }
        public String getP3() {
            return p3;
        }
    }

    @GetMapping("/ex/ex2")
    public String ex2(Model model) {
        log.info("/ex/ex2...............");
        List<String> strList = IntStream.range(1,10)
                .mapToObj(i -> "Data" + i)
                .toList();
        model.addAttribute("list", strList);

        Map<String, String> map = new HashMap<>();
        map.put("A","가가가가가");
        map.put("B","나나나나나");
        model.addAttribute("map", map);

        SampleDTO dto = new SampleDTO();
        dto.p1 = "값 ---- p1";
        dto.p2 = "값 ---- p2";
        dto.p3 = "값 ---- p3";
        model.addAttribute("dto", dto);

        return "ex2";
    }


    @GetMapping("/ex/ex3")
    public String ex3(String lang, Model model) {
        // en_US, ko_KR
        Locale locale = new Locale(lang);
        System.out.println("언어 변경: " + locale);
        messageSource.getMessage("app.title", null, locale);


        model.addAttribute("arr",
                new String[]{"호호호호","하하하핳","크크크크크"});
        // <p>%lt;b&gt;안녕,,, 타임리프 &lt;/b&gt;
        model.addAttribute("content","안녕,,, 타임리프");

        model.addAttribute("serverPort", serverPort);
        model.addAttribute("testPrice", testPrice);


        return "ex3";
    }

    @GetMapping("/ex/ex4")
    public String ex4(Model model) {

        model.addAttribute("arr",
                new String[]{"ㅋㅋㅋㅋㅋ","AAAAAA","BBBBBB"});
        model.addAttribute("param1", "gogo");
        model.addAttribute("param2", "zozo");

        return "ex4";
    }


    @GetMapping("/basic-objects/ex")
    public String basicObjects(
            HttpServletRequest request,
            HttpServletResponse response,
            HttpSession session,
            Model model) {
        model.addAttribute("contextPath", request.getContextPath());
        session.setAttribute("sessionData", "세션아 안뇽..");
        model.addAttribute("request", request);
        model.addAttribute("response", response);
        model.addAttribute("session", session);

        model.addAttribute("localDateTime", LocalDateTime.now());

        SampleDTO dto = new SampleDTO();
        dto.p1 = "호호호호호";
        dto.p2 = "하하하하하하";
        dto.p3 = "크크크크크킄";
        model.addAttribute("dto", dto);

        return "basic-objects";
    }

    @Component("helloBean")
    static class HelloBean {
        public String hello(String name) {
            return "hello " + name;
        }
    }

}
