package kroryi.spring.controller.api;

import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@Log4j2
@RestController
@RequestMapping("/api")
public class SampleAPIController {

    @ApiOperation("셈플 GET doA")
    @GetMapping("/sample/doA")
    public List<String> doA() {
        return Arrays.asList("AAAA","BBBB","CCCC");
    }
}
