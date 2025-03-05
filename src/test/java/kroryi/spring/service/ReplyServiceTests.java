package kroryi.spring.service;


import kroryi.spring.dto.ReplyDTO;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Log4j2
public class ReplyServiceTests {
    @Autowired
    private ReplyService replyService;

    @Test
    public void testRegister() {
        ReplyDTO dto = ReplyDTO.builder()
                .replyText("댓글 등록.. .테스트 1")
                .replier("댓글부대1")
                .bno(100L)
                .build();

        log.info(dto);
        log.info("등록된 댓글 번호: {}", replyService.register(dto));

    }





}
