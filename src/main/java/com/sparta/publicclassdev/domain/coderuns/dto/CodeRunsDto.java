package com.sparta.publicclassdev.domain.coderuns.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CodeRunsDto {

    private Long codeKatasId;
    private Long teamsId;
    private Long usersId;
    private Long responseTime;
    private String result;

    public CodeRunsDto(Long codeKatasId, Long teamsId, Long usersId, Long responseTime, String result) {
        this.codeKatasId = codeKatasId;
        this.teamsId = teamsId;
        this.usersId = usersId;
        this.responseTime = responseTime;
        this.result = result;
    }
}
