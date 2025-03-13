package kroryi.spring.dto;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.io.Serializable;
import java.util.Collection;

/**
 * DTO for {@link kroryi.spring.entity.Member}
 */
@ToString
@Getter
@Setter
public class MemberSecurityDTO extends User {
    String mid;
    String mpw;
    String email;
    Boolean del;
    Boolean social;

    public MemberSecurityDTO(String username,
                             String password, String email, Boolean del, Boolean social,
                             Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);

        this.mid = username;
        this.mpw = password;
        this.email = email;
        this.del = del;
        this.social = social;
    }
}