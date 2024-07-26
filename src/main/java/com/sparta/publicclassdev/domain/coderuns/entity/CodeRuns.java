package com.sparta.publicclassdev.domain.coderuns.entity;

import com.sparta.publicclassdev.domain.teams.entity.Teams;
import com.sparta.publicclassdev.global.entity.Timestamped;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "coderuns")
@Getter
@NoArgsConstructor
public class CodeRuns extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    private Double responseTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teams_id")
    private Teams teams;

    @Builder
    public CodeRuns(String code, Double responseTime, Teams teams) {
        this.code = code;
        this.responseTime = responseTime;
        this.teams = teams;
    }
}
