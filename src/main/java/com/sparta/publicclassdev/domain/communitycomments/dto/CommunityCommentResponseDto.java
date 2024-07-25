package com.sparta.publicclassdev.domain.communitycomments.dto;

import lombok.Getter;

@Getter
public class CommunityCommentResponseDto {

    String content;
    Long communityId;

    public CommunityCommentResponseDto(String content, Long communityId) {
        this.content = content;
        this.communityId = communityId;
    }

    public CommunityCommentResponseDto(String content){
        this.content = content;
    }
}
