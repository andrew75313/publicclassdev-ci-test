package com.sparta.publicclassdev.domain.codekatas.entity;

import com.sparta.publicclassdev.domain.winners.entity.Winners;
import com.sparta.publicclassdev.global.entity.Timestamped;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

@Entity
@Table(name = "codeKatas")
@Getter
@NoArgsConstructor
public class CodeKatas extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String contents;

    private LocalDate markDate;

    @OneToMany(mappedBy = "codeKatas")
    private List<Winners> winners;

    @Builder
    public CodeKatas(Long id, String contents, LocalDate markDate) {
        this.id = id;
        this.contents = contents;
        this.markDate = markDate;
    }

    public void updateContents(String contents) {
        this.contents = contents;
    }

    public void markCodeKatas(LocalDate markDate) {
        this.markDate = markDate;
    }
}
