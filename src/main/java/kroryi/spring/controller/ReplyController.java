package kroryi.spring.controller;


import io.swagger.annotations.ApiOperation;
import jakarta.validation.Valid;
import kroryi.spring.dto.PageRequestDTO;
import kroryi.spring.dto.PageResponseDTO;
import kroryi.spring.dto.ReplyDTO;
import kroryi.spring.service.ReplyService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindException;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/replies")
@Log4j2
public class ReplyController {

    @Autowired
    private ReplyService replyService;


    @ApiOperation(value="댓글 POST", notes = "PoST 방싱으로 댓글 등록")
    @PostMapping(value="/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String,Long> register(
            @Valid @RequestBody ReplyDTO replyDTO,
            BindingResult bindingResult
    )  throws BindException {

        log.info(replyDTO);

        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }
        Map<String, Long> resultMap = new HashMap<>();
        Long rno = replyService.register(replyDTO);
        resultMap.put("rno", rno);

        return resultMap;
    }

    @ApiOperation(value="댓글 목록", notes = "댓글 목록보기")
    @GetMapping(value="/list/{bno}")
    public PageResponseDTO<ReplyDTO> getList(@PathVariable("bno") Long bno,
                                             PageRequestDTO pageRequestDTO
                                             ) {
        PageResponseDTO<ReplyDTO> responseDTO = replyService.getListOfBoard(bno, pageRequestDTO);

        return responseDTO;
    }


    @ApiOperation(value="댓글 내용보기", notes = "댓글 내용보기")
    @GetMapping(value="/{rno}")
    public ReplyDTO getReplyDTO(@PathVariable("rno") Long rno) {

        return replyService.read(rno);
    }

    @ApiOperation(value="댓글 삭제", notes = "DELETE 댓글 삭제")
    @PreAuthorize("principal.username == #replyDTO.replier")
    @DeleteMapping(value="/{rno}", produces  = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Long> remove(@PathVariable("rno") Long rno,
                                    @RequestBody(required = false) ReplyDTO replyDTO,
                                    BindingResult bindingResult) throws BindException {
        log.info("-------------- {}",replyDTO);
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }
        replyService.remove(replyDTO.getRno());
        Map<String, Long> resultMap = new HashMap<>();
        resultMap.put("rno", replyDTO.getRno());
        return resultMap;
    }

    @ApiOperation(value="댓글 수정", notes = "PUT 방식 댓글 삭제")
    @PreAuthorize("principal.username == #replyDTO.replier") // 댓글 작성자 계정과 로그인 계정이 일치 검증
    @PutMapping(value="/{rno}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Long> modify(@PathVariable("rno") Long rno,
                                    @Valid @RequestBody ReplyDTO replyDTO,
                                    BindingResult bindingResult) throws BindException {
        log.info(replyDTO);
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }
        replyDTO.setRno(rno);
        replyService.modify(replyDTO);
        Map<String, Long> resultMap = new HashMap<>();
        resultMap.put("rno", rno);
        return resultMap;
    }


}
