package kroryi.spring.service;

import kroryi.spring.dto.BoardDTO;
import kroryi.spring.dto.BoardListReplyCountDTO;
import kroryi.spring.dto.PageRequestDTO;
import kroryi.spring.dto.PageResponseDTO;

public interface BoardService {

    Long register(BoardDTO dto);
    BoardDTO readOne(Long bno);
    void modify(BoardDTO dto);
    void remove(Long bno);
    
    PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO);
    PageResponseDTO<BoardListReplyCountDTO> listWithReplyCount(PageRequestDTO pageRequestDTO);

}
