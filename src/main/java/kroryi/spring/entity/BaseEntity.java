package kroryi.spring.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

//공용으로 다른 Entity에도 사용

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
public class BaseEntity {

    // updatable = false로 설정하면 regdate에 처음 레코드가 등록될때만 값이 입력되고
    // 그 다음부터는 값의 업데이트가 없는 설정
    @CreatedDate
    @Column(name="regdate", updatable = false)
    private LocalDateTime regDate;


    @LastModifiedDate
    @Column(name="moddate")
    private LocalDateTime modDate;
}
