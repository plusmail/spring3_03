package kroryi.spring.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 255)
    @Column(name = "city")
    private String city;

    @Size(max = 255)
    @Column(name = "street")
    private String street;

//    EAGER(즉시) LAZY(지연)
    // EAGER 디비쿼리 내용 그대로 가져오는 방식; 연관된 데이터를 한번에 조회
    // 불필요한 데이터가 조회될 가능성 있음.
    // SELECT문을 한번 실행
    // LAZY는 OneToMany에 사용하는 방식
    // SELECT문 여러번 실행 (부모를 로딩하고 후 연관된 자식객첵를 SELECT 더함.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    // 해당필드는 json으로 변환 하지 말라는 지지어.
    // 생각하면 다시 Employee를 json으로 변환 해서 맴돌이(순환) 발생
//    @JsonIgnore
    @JsonBackReference
    private Employee employee;

}