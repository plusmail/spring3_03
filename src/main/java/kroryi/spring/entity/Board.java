package kroryi.spring.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import java.util.HashSet;
import java.util.Set;


@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "imageSet")
//@NamedQuery(name="Board.findByTitle",
//        query="select e from board e where title like concat('%',e.title=:title,'%') ")
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long bno;

    @Column(nullable = false, length = 500)
    private String title;
    @Column(nullable = false, length = 2000)
    private String content;
    @Column(nullable = false, length = 50)
    private String writer;

    // Builder.builder().rno(100).name("홍길ㄷ").build();
    // 만약 Builder.Default 를 하지 않으면 imageSet이 null
    @OneToMany(mappedBy = "board",
            cascade = {CascadeType.ALL},
            fetch = FetchType.LAZY,
            orphanRemoval = true
    )
    @Builder.Default
    @BatchSize(size = 5) // 페이징 사이즈 만큼 숫자를 사용 한번 쿼리로 5개의 이미지 조회
    // ~~~ where board_image.board_bno in (?,?,?,?,?)
    private Set<BoardImage> imageSet = new HashSet<>();


    @OneToMany(mappedBy = "board",
            cascade = CascadeType.REMOVE,
            fetch = FetchType.LAZY,
            orphanRemoval = true)
    private Set<Reply> replySet = new HashSet<>();


    public void change(String title, String content) {
        this.title = title;
        this.content = content;
    }

    @PostLoad
    public void postLoad() {

    }

    public void addImage(String uuid, String fileName) {
        BoardImage image = BoardImage.builder()
                .uuid(uuid)
                .fileName(fileName)
                .board(this)
                .ord(imageSet.size())
                .build();
        imageSet.add(image);
    }

    public void clearImages() {
        imageSet.forEach(image -> image.changeBoard(null));
        this.imageSet.clear(); // clear() 쿼리에서는 delete  문 실행
    }
}
