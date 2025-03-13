package kroryi.spring.repository;

import kroryi.spring.entity.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {


    @EntityGraph(attributePaths = "roleSet")
    @Query("SELECT m FROM Member m WHERE m.mid = :mid and m.social = false ")
    Optional<Member> getWithRoles(String mid);


}
