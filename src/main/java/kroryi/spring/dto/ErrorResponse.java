package kroryi.spring.dto;
import lombok.*;
import java.util.Map;
@Data
public class ErrorResponse {
    private int status;
    private String message;
    private Map<String, String> errors; // 필드별 오류 메시지 저장

    public ErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public ErrorResponse(int status, String message, Map<String, String> errors) {
        this.status = status;
        this.message = message;
        this.errors = errors;
    }

    // Getter & Setter
}