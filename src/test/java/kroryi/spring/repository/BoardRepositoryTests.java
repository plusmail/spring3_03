package kroryi.spring.repository;


import jakarta.transaction.Transactional;
import kroryi.spring.dto.BoardListReplyCountDTO;
import kroryi.spring.entity.Board;
import kroryi.spring.entity.BoardImage;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class BoardRepositoryTests {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private ReplyRepository replyRepository;


    @Test
    public void testInsert() {
        IntStream.rangeClosed(1, 100).forEach(i -> {
            Board board = Board.builder()
                    .title("title(제목) --" + i)
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
        Long bno = 100L;
        // Optional<Board> 이렇게 하면 Board에 디비에서 가져온 데이터가 null 인지 아닌지 체크 하는 기능.
        // findby~~~ 되어 있는경우 find는 select문
        Optional<Board> result = boardRepository.findById(bno);
        Board board = result.orElseThrow();
        log.info(board.toString());
    }

    @Test
    public void testUpdate() {
        Long bno = 100L;
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
        Long bno = 100L;
        boardRepository.deleteById(bno);
    }

    @Test
    public void testPaging() {
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

    @Test
    public void testFindByTitle() {
        List<Board> boards = boardRepository.findByTitle("title(제목) --5");
        boards.forEach(board -> {
            log.info(board.toString());
        });
    }


    @Test
    public void testFindByTitleJPQL() {
        Pageable pageable = PageRequest.of(1, 5, Sort.by("bno").descending());

        Page<Board> boards = boardRepository.findByKeyword("--5", pageable);
        boards.forEach(board -> {
            log.info(board.toString());
        });
    }


    @Test
    public void testSearch1() {
        Pageable pageable = PageRequest.of(1, 5, Sort.by("bno").descending());
        boardRepository.search1(pageable);
    }

    @Test
    public void testSearchAll() {
        String[] types = {"t","w"};
        String keyword = "2";
        Pageable pageable = PageRequest.of(1, 5, Sort.by("bno").descending());
        Page<Board> result = boardRepository.searchAll(types, keyword, pageable);
        log.info(result.getTotalPages());
        log.info(result.getSize());
        log.info(result.getNumber());
        log.info("{} : {}", result.hasPrevious() , result.hasNext());
        result.forEach(board -> {
            log.info(board.toString());
        });

    }

    @Test
    public void testSearchReplyCount(){
        String[] types = {"t","c","w"};
        String keyword = "1";
        Pageable pageable = PageRequest.of(1,
                5,
                Sort.by("bno").descending()
        );

        Page<BoardListReplyCountDTO> result =
                boardRepository.searchWithReplyCount(types, keyword, pageable);


        log.info(result.getTotalPages());
        log.info(result.getSize());
        log.info(result.getNumber());
        log.info("{} : {}", result.hasPrevious() , result.hasNext());
        result.forEach(board -> {
            log.info(board.toString());
        });
    }


    @Test
    public void testInsertWithImages(){
        Board board = Board.builder()
                .title("이미지 테스틋")
                .content("첨부파일 테스트")
                .writer("user1")
                .build();

        for(int i=0; i < 3; i++){
            board.addImage(UUID.randomUUID().toString(), "file"+i+".jpg");
        }

        boardRepository.save(board);
    }

    @Test
    @Transactional
    public void testReadWithImages(){
        // fetch방식 (LAZY)는 SELECT 2번(Board 1번, BoardImage 1번)실행 N+1문제
        Optional<Board> result = boardRepository.findById(102L);
        Board board = result.orElseThrow();
        log.info(board.toString());
        // 연결하고 질의가 끝나면 연결을 끊어 버려서
        // 다음 boad.getImageSet() 쿼리가 생성되어야 하는데 연결이 끊어져서 실행 안됨.
        log.info("-----------------1");
        log.info(board.getImageSet());

    }

    @Test
    public void testReadWithImages2(){
        Optional<Board> result = boardRepository.findByIdWithImages(1L);
        Board board = result.orElseThrow();
        log.info(board.toString());
        log.info("-----------------");
        for(BoardImage image : board.getImageSet()){
            log.info(image.toString());
        }
    }

    @Test
    public void testReadWithImages3(){
        List<Object[]> result = boardRepository.findByIdWithImagesJQPL(1L);

        for(Object[] obj : result){
            log.info(Arrays.toString(obj));
        }

    }

    @Transactional
    @Commit
    @Test
    public void testModifyWithImages(){

        Optional<Board> result = boardRepository.findByIdWithImages(1L);
        Board board = result.orElseThrow();
        board.clearImages();

        for(int i=0; i < 2; i++){
            board.addImage(UUID.randomUUID().toString(), "file"+i+".jpg");
        }
        boardRepository.save(board);

    }


    @Test
    @Transactional
    @Commit
    public void testRemoveAll(){
        Long bno = 1L;
        replyRepository.deleteByBoard_Bno(bno);
        boardRepository.deleteById(bno);
    }




}
