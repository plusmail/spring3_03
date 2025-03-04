package kroryi.spring.entity;


import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
//@NamedQuery(name="Board.findByTitle",
//        query="select e from board e where title like concat('%',e.title=:title,'%') ")
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private int bno;

    @Column(nullable = false, length = 500)
    private String title;
    @Column(nullable = false, length = 2000)
    private String content;
    @Column(nullable = false, length = 50)
    private String writer;

    public void change(String title, String content){
        this.title = title;
        this.content = content;
    }

    @PostLoad
    public void postLoad() {

    }
}
