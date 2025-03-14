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

    @Column(name = "del", nullable = false)
    private boolean del;

    @Column(name = "social", nullable = false)
    private boolean social;

    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    private Set<MemberRole> roleSet = new HashSet<>();



    public void changePassword(String mpw){
        this.mpw = mpw;
    }

    public void changeEmail(String email){
        this.email = email;
    }

    public void changeDel(boolean del){
        this.del = del;
    }

    public void changeSocial(boolean social){
        this.social = social;
    }

    public void clearRoles(){
        this.roleSet.clear();
    }

    public void addRole(MemberRole role){
        this.roleSet.add(role);
    }

}