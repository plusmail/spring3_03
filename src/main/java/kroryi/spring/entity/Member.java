package kroryi.spring.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Builder
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "roleSet")
@Table(name = "member")
public class Member extends BaseEntity {
    @Id
    @Column(name = "mid", nullable = false)
    private String mid;

    @Column(name = "mpw")
    private String mpw;

    @Column(name = "email")
    private String email;

    @Column(name = "del")
    private Boolean del;

    @Column(name = "social")
    private Boolean social;

    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    private Set<MemberRole> roleSet = new HashSet<>();

    @PrePersist
    public void prePersist() {
        if(social == null) {
            social = false;
        }
    }

    public void changePassword(String mpw){
        this.mpw = mpw;
    }

    public void changeEmail(String email){
        this.email = email;
    }

    public void changeDel(Boolean del){
        this.del = del;
    }

    public void changeSocial(Boolean social){
        this.social = social;
    }

    public void clearRoles(){
        this.roleSet.clear();
    }

    public void addRole(MemberRole role){
        this.roleSet.add(role);
    }

}