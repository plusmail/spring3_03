package kroryi.spring.repository;

import jakarta.transaction.Transactional;
import kroryi.spring.entity.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {


    //  and m.social = false  해놓은 이유는 소셜로 가입된 사용자는 일반 로그인을 못하게 해놓은것
    @EntityGraph(attributePaths = "roleSet")
    @Query("SELECT m FROM Member m WHERE m.mid = :mid and m.social = false ")
    Optional<Member> getWithRoles(String mid);


    Optional<Member> findByEmail(String email);


    @Modifying
    @Transactional
    @Query("UPDATE Member m set m.mpw = :mpw where m.mid = :mid")
    void updatePassword(@Param("mid") String mid, @Param("mpw") String mpw);


    @Modifying
    @Transactional
    @Query("UPDATE Member m set m.mpw =:mpw , m.email=:email WHERE m.mid=:mid")
    void updateMember(@Param("mid")String mid, @Param("email") String email, @Param("mpw") String mpw);
}
