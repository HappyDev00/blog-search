package com.logan.bloginfo.util.webclient;

import com.logan.bloginfo.dto.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Component
public class NaverWebClient {

    @Value("${naver.api.url}") String baseUrl;
    @Value("${naver.api.client-id}") String clientId;
    @Value("${naver.api.client-secret}") String clientSecret;


    public Mono<BlogPostNaverResponseDTO> searchBlog(BlogSearchRequestNaverDTO blogSearchRequestNaverDTO) {

        WebClient webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("X-Naver-Client-Id", clientId)
                .defaultHeader("X-Naver-Client-Secret", clientSecret)
                .build();

        return webClient.get()
                .uri(uriBuilder -> {
                    UriBuilder builder = uriBuilder.path("/v1/search/blog.json")
                            .queryParam("query", blogSearchRequestNaverDTO.getQuery());

                    if (blogSearchRequestNaverDTO.getStart() != null) {
                        builder = builder.queryParamIfPresent("start", Optional.of(blogSearchRequestNaverDTO.getStart()));
                    }
                    if (blogSearchRequestNaverDTO.getDisplay() != null) {
                        builder = builder.queryParamIfPresent("display", Optional.of(blogSearchRequestNaverDTO.getDisplay()));
                    }
                    if (blogSearchRequestNaverDTO.getSort() != null) {
                        builder = builder.queryParamIfPresent("sort", Optional.of(blogSearchRequestNaverDTO.getSort().name().toLowerCase()));
                    }
                    return builder.build();
                })
                .retrieve()
                .bodyToMono(BlogPostNaverResponseDTO.class)
                .map(response -> {
                    List<BlogPostNaverDTO> items = response.getItems().stream()
                            .map(item ->
                                    BlogPostNaverDTO.builder()
                                        .title(item.getTitle())
                                        .bloggerlink(item.getBloggerlink())
                                        .bloggername(item.getBloggername())
                                        .description(item.getDescription())
                                        .postdate(item.getPostdate())
                                        .link(item.getLink())
                                        .build()
                            ).collect(Collectors.toList());

                    return BlogPostNaverResponseDTO.builder()
                            .display(response.getDisplay())
                            .start(response.getStart())
                            .lastBuildDate(response.getLastBuildDate())
                            .items(items)
                            .total(response.getTotal())
                            .build();
                });
    }



}
