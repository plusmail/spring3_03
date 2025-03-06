package kroryi.spring.repository;


import kroryi.spring.entity.Board;
import kroryi.spring.entity.Reply;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Log4j2
public class ReplyRepositoryTests {

    @Autowired
    private ReplyRepository repository;

    @Test
    public void  testInsert() {
        Long bno= 100L;

        Board board = Board.builder()
                .bno(bno)
                .build();

        Reply reply  = Reply.builder()
                .board(board)
                .replyText("댓글 .... sdf2dfsd")
                .replier("지나가는 sdf")
                .build();

        repository.save(reply);
    }

    @Test
    @Transactional
    public void testBoardReplies(){
        Long bno = 100L;
        Pageable pageable = PageRequest.of(
                0, 10,
                Sort.by("rno").descending());

        Page<Reply> replies = repository.listOfBoard(bno, pageable);
        replies.getContent().forEach(reply ->{
            log.info(replies.getContent().toString());
        });
    }

    @Test
    public void testInsertMultipleReplies() {
        Long bno = 99L;
        Board board = Board.builder()
                .bno(bno)
                .build();

        List<Reply> replies = new ArrayList<>();

        for (int i = 1; i <= 50; i++) {
            Reply reply = Reply.builder()
                    .board(board)
                    .replyText("댓글...." + i)
                    .replier("지나가는 사람" + i)
                    .build();
            replies.add(reply);
        }

        repository.saveAll(replies);
    }
}
