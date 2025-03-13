package kroryi.spring.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class MemberJoinDTO {
    private String mid;
    private String mpw;
    private String re_mpw;
    private String email;
    private boolean del;
    private boolean social;
}
