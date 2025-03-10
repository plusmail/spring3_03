package kroryi.spring.service;


import kroryi.spring.dto.BoardDTO;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.UUID;

@SpringBootTest
@Log4j2
public class BoardServiceTest {
    @Autowired
    private BoardService boardService;

    @Test
    public void testRegisterWithImages(){
        log.info(boardService.getClass().getName());
        BoardDTO boardDTO = BoardDTO.builder()
                .title("title---image with")
                .content("content---image with")
                .writer("writer--image with")
                .build();

        boardDTO.setFileNames(
                Arrays.asList(
                        UUID.randomUUID() + "_aaa.jpg",
                        UUID.randomUUID() + "_bbb.jpg",
                        UUID.randomUUID() + "_ccc.jpg"
                ));

        Long bno = boardService.register(boardDTO);
        log.info("bno: {} 등록성공", bno);
    }


    @Test
    public void testReadAll(){
        Long bno = 101L;
        BoardDTO boardDTO = boardService.readOne(bno);
        log.info(boardDTO.toString());

        for(String fileName: boardDTO.getFileNames()) {
            log.info(fileName);
        }
    }

    @Test
    public void testModify(){
        BoardDTO boardDTO = BoardDTO.builder()
                .bno(101L)
                .title("title---modify")
                .content("content---modify")
                .build();

        boardDTO.setFileNames(Arrays.asList(UUID.randomUUID()+"_zzz.jpg"));
        boardService.modify(boardDTO);
    }

    @Test
    public void testRemoveAll(){
        Long bno = 101L;
        boardService.remove(bno);
    }

}
