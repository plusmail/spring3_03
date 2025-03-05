package kroryi.spring.repository.search;

import kroryi.spring.dto.BoardListReplyCountDTO;
import kroryi.spring.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardSearch {
    Page<Board> search1(Pageable pageable);

    Page<Board> searchAll(String[] types, String keyword, Pageable pageable);

    Page<BoardListReplyCountDTO> searchWithReplyCount(
            String[] types,
            String keyword,
            Pageable pageable);

}
