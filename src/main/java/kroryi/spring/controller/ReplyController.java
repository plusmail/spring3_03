package kroryi.spring.controller;


import io.swagger.annotations.ApiOperation;
import jakarta.validation.Valid;
import kroryi.spring.dto.ReplyDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.BindException;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/replies")
@Log4j2
public class ReplyController {



    @ApiOperation(value="댓글 POST", notes = "PoST 방싱으로 댓글 등록")
    @PostMapping(value="/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> register(
            @Valid @RequestBody ReplyDTO replyDTO,
            BindingResult bindingResult
    )  throws BindException {

        log.info(replyDTO);

        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }
        return ResponseEntity.ok("e등록 성공");
    }

}
