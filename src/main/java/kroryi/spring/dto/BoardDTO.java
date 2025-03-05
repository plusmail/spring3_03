package kroryi.spring.dto;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link kroryi.spring.entity.Board}
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardDTO implements Serializable {
    private Long bno;
    private String title;
    private String content;
    private String writer;
    private LocalDateTime regDate;
    private LocalDateTime modDate;
}