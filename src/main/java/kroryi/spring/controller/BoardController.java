package kroryi.spring.controller;

import jakarta.validation.Valid;
import kroryi.spring.dto.*;
import kroryi.spring.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@Log4j2
@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {
    @Value("${spring.servlet.multipart.location}")
    private String location;

    private final BoardService boardService;

    @GetMapping("/list")
    public String list(@Valid PageRequestDTO pageRequestDTO,
                       BindingResult bindingResult,
                       Model model) {

        if (bindingResult.hasErrors()) {
            pageRequestDTO = PageRequestDTO.builder().build();
        }

//        PageResponseDTO<BoardDTO> pageResponseDTO = boardService.list(pageRequestDTO);
        PageResponseDTO<BoardListAllDTO> pageResponseDTO =
                boardService.listWithAll(pageRequestDTO);

        model.addAttribute("resDTO", pageResponseDTO);
        model.addAttribute("reqDTO", pageRequestDTO);

        return "/board/list";
    }

    @GetMapping("/register")
    @PreAuthorize("isAuthenticated()") // 로그인된 사용자 모두 접근
    public String register() {

        return "board/register";
    }

    @PostMapping("/register")
    public String registerPost(@Valid BoardDTO boardDTO,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {
        log.info("------------register============");
        if (bindingResult.hasErrors()) {
            log.info("에러발생");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());

            return "redirect:/board/register";
        }

        Long bno = boardService.register(boardDTO);
        log.info(boardDTO);
        redirectAttributes.addFlashAttribute("result", bno);


        return "redirect:/board/list";
    }

    @GetMapping("/read/{bno}")
    public String read(@PathVariable Long bno, PageRequestDTO pageRequestDTO, Model model) {
        BoardDTO boardDTO = boardService.readOne(bno);

        log.info(boardDTO);

        model.addAttribute("dto", boardDTO);
        model.addAttribute("reqDTO", pageRequestDTO);
        return "board/read";
    }

    //    @PreAuthorize("permitAll()") // 모든 사용자
//    @PreAuthorize("isAuthenticated()") // 로그인된 사용자 모두 접근
//    @PreAuthorize("hasAuthority('ROLE_USER') and principal.username == #boardDTO.writer")
//    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN') and principal.username == #boardDTO.writer")
    @PreAuthorize("hasAnyRole('USER','ADMIN') and principal.username == #boardDTO.writer")
    @PostMapping("/modify")
    public String modify(@Valid BoardDTO boardDTO,
                         PageRequestDTO pageRequestDTO,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {
        log.info("-------modify post -------");
        if (bindingResult.hasErrors()) {
            log.info("/modify post 에러 발생 : {}", bindingResult.getAllErrors());
            String link = pageRequestDTO.getLink();
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            redirectAttributes.addAttribute("bno", boardDTO.getBno());
            return "redirect:/board/modify?" + link;
        }
        boardService.modify(boardDTO);
        redirectAttributes.addFlashAttribute("result", "수정됨");

        redirectAttributes.addAttribute("bno", boardDTO.getBno());
        return "redirect:/board/read/" + boardDTO.getBno();
    }

    @GetMapping("/modify/{bno}")
    public String modify(@PathVariable Long bno, PageRequestDTO pageRequestDTO, Model model) {
        log.info("-------modify -------");
        BoardDTO boardDTO = boardService.readOne(bno);
        log.info("/board/modify ---> {}", boardDTO);
        model.addAttribute("dto", boardDTO);
        model.addAttribute("reqDTO", pageRequestDTO);
        return "board/modify";
    }

    @PreAuthorize("principal.username == #boardDTO.writer")
    @PostMapping("/remove")
    public String remove(BoardDTO boardDTO, RedirectAttributes redirectAttributes) {

        Long bno = boardDTO.getBno();
        boardService.remove(bno);

        log.info(boardDTO.getFileNames());

        List<String> fileNames = boardDTO.getFileNames();
        if (fileNames != null && fileNames.size() > 0) {
            removeFiles(fileNames);
        }

        redirectAttributes.addFlashAttribute("result", "삭제되었습니다.");
        return "redirect:/board/list";
    }

    public void removeFiles(List<String> fileNames) {
        for (String fileName : fileNames) {
            Resource resource = new FileSystemResource(location + File.separator + fileName);

            String resourceName = resource.getFilename();
            try {
                String contentType = Files.probeContentType(resource.getFile().toPath());
                resource.getFile().delete();
                if (contentType.startsWith("image")) {
                    File thumbnailFile = new File(location + File.separator + "s_" + fileName);
                    thumbnailFile.delete();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }
}
