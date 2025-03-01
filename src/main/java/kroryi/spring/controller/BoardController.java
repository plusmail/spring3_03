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
import org.springframework.web.bind.annotation.RequestMapping;

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

}
