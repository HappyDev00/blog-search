package com.logan.bloginfo.util.webclient;

import com.logan.bloginfo.dto.BlogPostDTO;
import com.logan.bloginfo.dto.BlogSearchRequestDTO;
import com.logan.bloginfo.dto.BlogSearchResultDTO;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Component
public class KakaoWebClient {

    @Value("${kakao.api.url}") String baseUrl;
    @Value("${kakao.api.key}") String apiKey;


    public Mono<BlogSearchResultDTO> searchBlog(BlogSearchRequestDTO blogSearchRequestDTO) {


        WebClient webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "KakaoAK " + apiKey)
                .build();

        return webClient.get()
                .uri(uriBuilder -> {
                    UriBuilder builder = uriBuilder.path("/v2/search/blog")
                            .queryParam("query", blogSearchRequestDTO.getQuery());

                    if (blogSearchRequestDTO.getPage() != null) {
                        builder = builder.queryParamIfPresent("page", Optional.of(blogSearchRequestDTO.getPage()));
                    }
                    if (blogSearchRequestDTO.getSize() != null) {
                        builder = builder.queryParamIfPresent("size", Optional.of(blogSearchRequestDTO.getSize()));
                    }
                    if (blogSearchRequestDTO.getSort() != null) {
                        builder = builder.queryParamIfPresent("sort", Optional.of(blogSearchRequestDTO.getSort().name().toLowerCase()));
                    }
                    return builder.build();
                })
                .retrieve()
                .bodyToMono(BlogSearchResultDTO.class)
                .map(response -> {
                    List<BlogPostDTO> blogs = response.getDocuments().stream()
                            .map(document ->
                                BlogPostDTO.builder()
                                        .url(document.getUrl())
                                        .title(document.getTitle())
                                        .contents(document.getContents())
                                        .datetime(document.getDatetime())
                                        .build()
                            ).collect(Collectors.toList());

                    return BlogSearchResultDTO.builder()
                            .documents(blogs)
                            .build();
                });
    }

}
