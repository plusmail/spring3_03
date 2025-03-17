package kroryi.spring.dto;


import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

//@Data는
//@Getter
//@Setter
//@ToString
//@EqualsAndHashCode
//@RequiredArgsConstructor
// APIUserDTO 생성자를 직접 구현을 해야 하므로 @RequiredArgsConstructor 사용불가
@Getter
@Setter
@ToString
public class APIUserDTO extends User {
    private String mid;
    private String mpw;

    public APIUserDTO(String username,
                      String password,
                      Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);

        this.mid = username;
        this.mpw = password;
    }
}
