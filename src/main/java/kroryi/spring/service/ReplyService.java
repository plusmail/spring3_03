package kroryi.spring.service;

import kroryi.spring.dto.PageRequestDTO;
import kroryi.spring.dto.PageResponseDTO;
import kroryi.spring.dto.ReplyDTO;
import org.springframework.data.domain.Pageable;

public interface ReplyService {
    Long register(ReplyDTO dto);

    ReplyDTO read(Long rno);
    void modify(ReplyDTO dto);
    void remove(Long rno);

    PageResponseDTO<ReplyDTO> getListOfBoard(Long bno, PageRequestDTO pageRequestDTO);

}
