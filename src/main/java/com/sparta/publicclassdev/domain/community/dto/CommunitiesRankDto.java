package com.sparta.publicclassdev.domain.community.dto;

import lombok.Getter;

@Getter
public class CommunitiesRankDto {
    String keyword;

    public CommunitiesRankDto(String keyword) {
        this.keyword = keyword;
    }
}
