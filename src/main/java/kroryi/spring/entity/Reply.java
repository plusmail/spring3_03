package kroryi.spring.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = "board")
@Table(name = "reply", indexes = {
        @Index(name = "idx_reply_board_bno", columnList = "board_bno")
})
public class Reply extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rno", nullable = false)
    private Long rno;

    private String replyText;

    @Column(name = "replier")
    private String replier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_bno", nullable = false)
    private Board board;

    public void changeText(String replyText) {
        this.replyText = replyText;
    }

    public void changeReplier(String replier) {
        this.replier = replier;
    }

    public void changeReplierAndReplyText(String replier, String replyText) {
        this.replyText = replyText;
        this.replier = replier;
    }

}