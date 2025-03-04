package kroryi.spring.config;

import jakarta.servlet.http.HttpServletRequest;
import kroryi.spring.controller.BoardController;
import kroryi.spring.controller.EmployeeController;
import kroryi.spring.controller.ReplyController;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Log4j2
public class CustomRestAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value= {Exception.class}) // value에 잡고자 하는 클래스 지정
    public ResponseEntity exception(Exception e, HttpServletRequest request) {

        if (request.getRequestURI().startsWith("/api/api-docs") ||
                request.getRequestURI().startsWith("/swagger-ui")) {
            return ResponseEntity.status(500).build(); // Swagger의 기본 예외 처리를 유지
        }
        log.error("RestApiExceptionHandler", e);
        return ResponseEntity.status(200).build();
    }

    @ExceptionHandler(value = { IndexOutOfBoundsException.class})
    public ResponseEntity outOfBound(
            IndexOutOfBoundsException e , HttpServletRequest request
    ){

        if (request.getRequestURI().startsWith("/api/api-docs") ||
                request.getRequestURI().startsWith("/swagger-ui")) {
            return ResponseEntity.status(500).build(); // Swagger의 기본 예외 처리를 유지
        }
        log.error("IndexOutOfBoundsException", e);
        return ResponseEntity.status(200).build();
    }
}