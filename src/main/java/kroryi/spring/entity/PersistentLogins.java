package kroryi.spring.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PersistentLogins {

    @Id
    private String series;

    private String username;
    private String token;
    private LocalDateTime lastUsed;
}
