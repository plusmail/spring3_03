package kroryi.spring.repository;


import kroryi.spring.entity.Board;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class BoardRepositoryTests {

    @Autowired
    private BoardRepository boardRepository;


    @Test
    public void testInsert() {
        IntStream.rangeClosed(1,100).forEach(i->{
            Board board = Board.builder()
                    .title("title(제목) --"+i)
                    .content("content..." + i)
                    .writer("user" + (i % 10))
                    .build();

            // .save 는 sql는 insert문
            Board result = boardRepository.save(board);
            log.info("BNO: " + result.getBno());

        });
    }

    @Test
    public void testSelect() {
        int bno = 100;
        // Optional<Board> 이렇게 하면 Board에 디비에서 가져온 데이터가 null 인지 아닌지 체크 하는 기능.
        // findby~~~ 되어 있는경우 find는 select문
        Optional<Board> result = boardRepository.findById(bno);
        Board board = result.orElseThrow();
        log.info(board.toString());
    }

    @Test
    public void testUpdate() {
        int bno = 100;
        // Optional<Board> 이렇게 하면 Board에 디비에서 가져온 데이터가 null 인지 아닌지 체크 하는 기능.
        // findby~~~ 되어 있는경우 find는 select문
        Optional<Board> result = boardRepository.findById(bno);
        Board board = result.orElseThrow();

        log.info(board.toString());

        board.change("update title 100으로 변경", "update content 100 수정됨.");
        boardRepository.save(board);

    }

    @Test
    public void testDelete() {
        int bno = 100;
        boardRepository.deleteById(bno);
    }

    @Test
    public void testPaging(){
        Pageable pageable = PageRequest.of(1, 5, Sort.by("bno").descending());

        Page<Board> result = boardRepository.findAll(pageable);

        log.info("Total count :" + result.getTotalElements());
        log.info("Total pages :" + result.getTotalPages());
        log.info("page number :" + result.getNumber());
        log.info("page size :" + result.getSize());

        List<Board> boards = result.getContent();

        boards.forEach(board -> {
            log.info(board.toString());
        });


    }



}
