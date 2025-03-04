package kroryi.spring.controller;

import jakarta.validation.Valid;
import kroryi.spring.dto.BoardDTO;
import kroryi.spring.dto.PageRequestDTO;
import kroryi.spring.dto.PageResponseDTO;
import kroryi.spring.entity.Board;
import kroryi.spring.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
@Log4j2
@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/list")
    public String list(@Valid PageRequestDTO pageRequestDTO,
                       BindingResult bindingResult,
                       Model model) {

        if(bindingResult.hasErrors()) {
            pageRequestDTO = PageRequestDTO.builder().build();
        }

        PageResponseDTO<BoardDTO> pageResponseDTO = boardService.getBoardsSearch(pageRequestDTO);

        model.addAttribute("resDTO", pageResponseDTO);
        model.addAttribute("reqDTO", pageRequestDTO);

        return "/board/list";
    }
    @GetMapping("/register")
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

        int bno = boardService.register(boardDTO);
        log.info(boardDTO);
        redirectAttributes.addFlashAttribute("result", bno);


        return "redirect:/board/list";
    }

    @GetMapping("/read/{bno}")
    public String read(@PathVariable int bno, PageRequestDTO pageRequestDTO, Model model) {
        BoardDTO boardDTO = boardService.readOne(bno);

        log.info(boardDTO);

        model.addAttribute("dto", boardDTO);
        model.addAttribute("reqDTO", pageRequestDTO);
        return "board/read";
    }

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
        return "redirect:/board/read/"+boardDTO.getBno();
    }
    @GetMapping("/modify/{bno}")
    public String modify(@PathVariable int bno, PageRequestDTO pageRequestDTO, Model model) {
        log.info("-------modify -------");
        BoardDTO boardDTO = boardService.readOne(bno);
        log.info("/board/modify ---> {}", boardDTO);
        model.addAttribute("dto", boardDTO);
        model.addAttribute("reqDTO", pageRequestDTO);
        return "board/modify";
    }
    @PostMapping("/remove/{bno}")
    public String remove(@PathVariable int bno, RedirectAttributes redirectAttributes) {
        boardService.remove(bno);
        redirectAttributes.addFlashAttribute("result", "삭제되었습니다.");
        return "redirect:/board/list";
    }
}
