package com.logan.bloginfo.service;

import com.logan.bloginfo.dto.*;
import com.logan.bloginfo.util.webclient.KakaoWebClient;
import com.logan.bloginfo.util.webclient.NaverWebClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class BlogSearchService {
    private final KakaoWebClient kakaoWebClient;
    private final NaverWebClient naverWebClient;

    public Mono<BlogSearchResultDTO> search(BlogSearchRequestDTO blogSearchRequestDTO) {
         log.info("request kakao");
         return kakaoWebClient.searchBlog(blogSearchRequestDTO)
                .onErrorResume(error -> {

                    log.debug(error.getMessage());
                    log.info("kakao request failed...request naver");

                    return naverWebClient.searchBlog(BlogSearchRequestNaverDTO.builder()
                                    .query(blogSearchRequestDTO.getQuery())
                                    .sort(blogSearchRequestDTO.getSort()!=null ?
                                            (blogSearchRequestDTO.getSort() == BlogSortType.ACCURACY ? BlogSortNaverType.SIM : BlogSortNaverType.DATE) : null)
                                    .display(blogSearchRequestDTO.getSize())
                                    .start(blogSearchRequestDTO.getPage())
                                    .build())
                            .map(naverResponse -> {
                                List<BlogPostDTO> documents = naverResponse.getItems().stream().map(item -> BlogPostDTO.builder()
                                        .url(item.getLink())
                                        .title(item.getTitle())
                                        .contents(item.getDescription())
                                        .datetime(item.getPostdate()).build())
                                        .collect(Collectors.toList());

                                return BlogSearchResultDTO.builder()
                                        .documents(documents)
                                        .build();
                            });
                })
                .onErrorResume(error -> {
                    log.info("Naver request failed.");
                    log.debug(error.getMessage());
                     throw new RuntimeException("Failed to retrieve blog posts", error);
                 });
    }
}