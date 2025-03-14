package kroryi.spring.repository;

import kroryi.spring.entity.Member;
import kroryi.spring.entity.MemberRole;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Commit;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class MemberRepositoryTests {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void insertMember() {
        IntStream.range(0, 40).forEach(i -> {
            Member member = Member.builder()
                    .mid("member" + i)
                    .mpw(passwordEncoder.encode("1111"))
                    .email("email" + i + "@email.com")
                    .del(false)
                    .social(false)
                    .build();
            if( i>= 35){
                member.addRole(MemberRole.ADMIN);
            }else{
                member.addRole(MemberRole.USER);
            }
            memberRepository.save(member);
        });
    }

    @Test
    public void readMember() {
        Optional<Member> result = memberRepository.getWithRoles("member39");
        Member member = result.orElseThrow();
        log.info(member);
        log.info(member.getRoleSet());

        member.getRoleSet().forEach(role ->{
            log.info(role.name());
        });


    }

    @Commit
    @Test
    public void testUpdate(){
        String mid = "plus4957@naver.com";
        String mpw = passwordEncoder.encode("1111");
        memberRepository.updatePassword(mid, mpw);
    }


}
