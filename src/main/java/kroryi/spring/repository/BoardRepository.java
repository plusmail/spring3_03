package kroryi.spring.repository;

import kroryi.spring.entity.Board;
import kroryi.spring.repository.search.BoardSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Integer>, BoardSearch {


    @Query(value = "SELECT now()", nativeQuery = true)
    String getTime();


    // findBy select문
    List<Board> findByTitle(String title);

    List<Board> findByTitleLike(String title);

    List<Board> findByTitleContainingOrderByBno(String title);

    List<Board> findByContent(String content);

    List<Board> findByWriter(String writer);

    List<Board> findByRegDateAfter(LocalDateTime regDate);

    List<Board> findByTitleContainingOrContentContaining(String title, String content);

    //    JPQL 은 약간의(join) 관계에 사용에 유리
    @Query("SELECT b FROM Board b WHERE b.title like concat('%',:keyword, '%')")
    Page<Board> findByKeyword(@Param("keyword") String keyword, Pageable pageable);

}
