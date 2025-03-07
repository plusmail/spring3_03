package kroryi.spring.entity;


import jakarta.persistence.*;
import lombok.*;

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
    private Set<BoardImage> imageSet = new HashSet<>();

    public void change(String title, String content){
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
        this.imageSet.clear();
    }
}
