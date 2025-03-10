package kroryi.spring.service;

import kroryi.spring.dto.*;
import kroryi.spring.entity.Board;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public interface BoardService {

    Long register(BoardDTO dto);
    BoardDTO readOne(Long bno);
    void modify(BoardDTO dto);
    void remove(Long bno);
    
    PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO);
    PageResponseDTO<BoardListReplyCountDTO> listWithReplyCount(PageRequestDTO pageRequestDTO);

    PageResponseDTO<BoardListAllDTO> listWithAll(PageRequestDTO pageRequestDTO);

    default Board dtoToEntity(BoardDTO dto){

        Board board = Board.builder()
                .bno(dto.getBno())
                .title(dto.getTitle())
                .content(dto.getContent())
                .writer(dto.getWriter())
                .build();

        if(dto.getFileNames() != null){
            dto.getFileNames().forEach(fileName -> {
                String[] arr = fileName.split("_");
                board.addImage(arr[0], arr[1]);
            });
        }
        return board;
    }

    default BoardDTO entityToDto(Board board){
        BoardDTO boardDTO = BoardDTO.builder()
                .bno(board.getBno())
                .title(board.getTitle())
                .content(board.getContent())
                .writer(board.getWriter())
                .regDate(board.getRegDate())
                .modDate(board.getModDate())
                .build();

        List<String> fileNames =
                board.getImageSet().stream().sorted().map(image ->{
                    return image.getUuid()+"_" + image.getFileName();
                }).toList();

        boardDTO.setFileNames(fileNames);

        return boardDTO;
    }


}
