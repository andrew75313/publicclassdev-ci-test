package com.sparta.publicclassdev.domain.community.service;

import com.sparta.publicclassdev.domain.community.dto.CommunitiesRankDto;
import com.sparta.publicclassdev.domain.community.dto.CommunitiesRequestDto;
import com.sparta.publicclassdev.domain.community.dto.CommunitiesResponseDto;
import com.sparta.publicclassdev.domain.community.dto.CommunitiesUpdateRequestDto;
import com.sparta.publicclassdev.domain.community.entity.Communities;
import com.sparta.publicclassdev.domain.community.repository.CommunitiesRepository;
import com.sparta.publicclassdev.domain.communitycomments.dto.CommunityCommentResponseDto;
import com.sparta.publicclassdev.domain.communitycomments.entity.CommunityComments;
import com.sparta.publicclassdev.domain.users.entity.Users;
import com.sparta.publicclassdev.global.exception.CustomException;
import com.sparta.publicclassdev.global.exception.ErrorCode;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommunitiesService {
    private final CommunitiesRepository repository;
    private final RedisTemplate<String, String> redisTemplate;


    public CommunitiesResponseDto createPost(CommunitiesRequestDto requestDto, Users user) {
        Communities community = Communities.builder()
            .title(requestDto.getTitle())
            .content(requestDto.getContent())
            .category(requestDto.getCategory())
            .user(user)
            .build();

        repository.save(community);

        return new CommunitiesResponseDto(requestDto.getTitle(), requestDto.getContent(), requestDto.getCategory());
    }

    public CommunitiesResponseDto updatePost(Long communityId, CommunitiesUpdateRequestDto requestDto) {
        Communities community = checkCommunity(communityId);

        community.updateContent(requestDto.getContent());
        repository.save(community);
        return new CommunitiesResponseDto(community.getTitle(), community.getContent(), community.getCategory());
    }

    public void deletePost(Long communityId, Users user) {
        Communities community = checkCommunity(communityId);

        if(community.getUser() != user){
            throw new CustomException(ErrorCode.NOT_UNAUTHORIZED);
        }

        repository.delete(community);
    }

    public List<CommunitiesResponseDto> findPosts() {
        List<Communities> postList = repository.findAll();
        return postList.stream().map(communities -> new CommunitiesResponseDto(communities.getTitle(), communities.getContent(), communities.getCategory()))
            .collect(Collectors.toList());
    }

    public CommunitiesResponseDto findPost(Long communityId) {
        Communities community = checkCommunity(communityId);
        List<CommunityComments> commentsList = community.getCommentsList();
        List<CommunityCommentResponseDto> responseDto = commentsList.stream().map(communityComments -> new CommunityCommentResponseDto(communityComments.getContent()))
            .toList();

        return new CommunitiesResponseDto(community.getTitle(), community.getContent(), community.getCategory(), community.getUser().getName(), responseDto);
    }

    public Communities checkCommunity(Long communityId){
        return repository.findById(communityId).orElseThrow(
            () -> new CustomException(ErrorCode.NOT_FOUND_COMMUNITY_POST)
        );
    }


    public List<CommunitiesResponseDto> searchPost(String keyword, int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<Communities> communityPage = repository.findByTitleContainingIgnoreCase(keyword, pageable);

        if(!communityPage.isEmpty()){
            redisTemplate.opsForZSet().incrementScore("searchRank",keyword,1);
        }

        return communityPage.stream()
            .map(communities -> new CommunitiesResponseDto(communities.getTitle(), communities.getContent(), communities.getCategory()))
            .collect(Collectors.toList());
    }

    public List<CommunitiesRankDto> rank() {
        String key = "searchRank";
        ZSetOperations<String, String> zSetOperations = redisTemplate.opsForZSet();
        Set<ZSetOperations.TypedTuple<String>> typedTuples = zSetOperations.reverseRangeByScoreWithScores(key, 0, 5);
        return typedTuples.stream().map(typedTuple -> new CommunitiesRankDto(typedTuple.getValue())).toList();
    }
}
