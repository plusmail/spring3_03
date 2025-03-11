package kroryi.spring.service;


import kroryi.spring.dto.*;
import kroryi.spring.entity.Board;
import kroryi.spring.repository.BoardRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.Optional;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class BoardServiceImpl implements BoardService {
    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private ModelMapper modelMapper;

    public PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO) {

        String[] types = pageRequestDTO.getTypes();
        String keyword = pageRequestDTO.getKeyword();
        Pageable pageable = pageRequestDTO.getPageable("bno");

        Page<Board> result = boardRepository.searchAll(types, keyword, pageable);

        List<BoardDTO> dtoList = result.getContent().stream()
                .map(board -> modelMapper.map(board, BoardDTO.class))
                .collect(Collectors.toList());

        return PageResponseDTO.<BoardDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total((int) result.getTotalElements())
                .build();
    }


    public Long register(BoardDTO boardDTO) {
//        Board board = modelMapper.map(boardDTO, Board.class);
        Board board = dtoToEntity(boardDTO);
        return boardRepository.save(board).getBno();
    }

    public BoardDTO readOne(Long bno) {
//        Optional<Board> result = boardRepository.findById(bno);

        Optional<Board> result = boardRepository.findByIdWithImages(bno);
        Board board = result.orElseThrow();

//        BoardDTO boardDTO = modelMapper.map(board, BoardDTO.class);
        BoardDTO boardDTO = entityToDto(board);

        return boardDTO;

    }

    public void modify(BoardDTO dto) {
        Optional<Board> result = boardRepository.findById(dto.getBno());
        Board board = result.orElseThrow();
        board.change(dto.getTitle(), dto.getContent());

        board.clearImages();
        if(dto.getFileNames() != null) {
            for(String fileName : dto.getFileNames()) {
                String[] arr = fileName.split("_");
                // arr[0]는 uuid, arr[1] aaa.jpg(파일명)
                board.addImage(arr[0], arr[1]);
            }
        }

        boardRepository.save(board);

    }

    public void remove(Long id) {
        boardRepository.deleteById(id);
    }


    @Override
    public PageResponseDTO<BoardListReplyCountDTO> listWithReplyCount(PageRequestDTO pageRequestDTO) {

        String[] types = pageRequestDTO.getTypes();
        String keyword = pageRequestDTO.getKeyword();
        Pageable pageable = pageRequestDTO.getPageable("bno");

        // 기본에 했던 ModelMapper을 사용 않해도됨. Projections.beans로 DTO로 이미 쿼리 결과를 변환된것임.
        Page<BoardListReplyCountDTO> result =
                boardRepository.searchWithReplyCount(types, keyword, pageable);


        return PageResponseDTO.<BoardListReplyCountDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(result.getContent())
                .total((int) result.getTotalElements())
                .build();
    }

    @Override
    public PageResponseDTO<BoardListAllDTO> listWithAll(PageRequestDTO pageRequestDTO) {
        String[] types = pageRequestDTO.getTypes();
        String keyword = pageRequestDTO.getKeyword();
        Pageable pageable = pageRequestDTO.getPageable("bno");
        Page<BoardListAllDTO> result = boardRepository.searchWithAll(types, keyword, pageable);

        return PageResponseDTO.<BoardListAllDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(result.getContent())
                .total((int)result.getTotalElements())
                .build();
    }
}
