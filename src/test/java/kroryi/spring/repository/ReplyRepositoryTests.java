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

@SpringBootTest
@Log4j2
public class ReplyRepositoryTests {

    @Autowired
    private ReplyRepository repository;

    @Test
    public void  testInsert() {
        Long bno= 2L;

        Board board = Board.builder()
                .bno(bno)
                .build();

        Reply reply  = Reply.builder()
                .board(board)
                .replyText("댓글 .... sdf2233")
                .replier("지나가는 사람sdf22233")
                .build();

        repository.save(reply);
    }

    @Test
    @Transactional
    public void testBoardReplies(){
        Long bno = 1L;
        Pageable pageable = PageRequest.of(
                0, 10,
                Sort.by("rno").descending());

        Page<Reply> replies = repository.listOfBoard(bno, pageable);
        replies.getContent().forEach(reply ->{
            log.info(replies.getContent().toString());
        });
    }
}
