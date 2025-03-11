package kroryi.spring.controller;


import jakarta.validation.Valid;
import kroryi.spring.dto.BoardListAllDTO;
import kroryi.spring.dto.PageRequestDTO;
import kroryi.spring.dto.PageResponseDTO;
import kroryi.spring.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Log4j2
@Controller
@RequestMapping("")
@RequiredArgsConstructor
public class HomeController {
    private final BoardService boardService;
    @GetMapping("")
    public String home(@Valid PageRequestDTO pageRequestDTO,
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
}
