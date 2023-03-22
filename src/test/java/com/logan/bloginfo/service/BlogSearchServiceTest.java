package com.logan.bloginfo.service;

import com.logan.bloginfo.dto.BlogPostNaverDTO;
import com.logan.bloginfo.dto.BlogPostNaverResponseDTO;
import com.logan.bloginfo.dto.BlogSearchRequestDTO;
import com.logan.bloginfo.dto.BlogSearchResultDTO;
import com.logan.bloginfo.util.webclient.KakaoWebClient;
import com.logan.bloginfo.util.webclient.NaverWebClient;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@Slf4j
public class BlogSearchServiceTest {

    @Mock
    private KakaoWebClient kakaoWebClient;

    @Mock
    private NaverWebClient naverWebClient;

    @InjectMocks
    private BlogSearchService blogSearchService;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void searchShouldReturnKakaoResults() {

        BlogSearchRequestDTO requestDTO = BlogSearchRequestDTO.builder().query("손흥민 축구선수").build();
        when(kakaoWebClient.searchBlog(requestDTO)).thenReturn(Mono.just(BlogSearchResultDTO.builder().documents(Collections.emptyList()).build()));

        BlogSearchResultDTO result = blogSearchService.search(requestDTO).block();

        assertThat(result).isNotNull();
        verify(kakaoWebClient).searchBlog(requestDTO);
        verify(naverWebClient, never()).searchBlog(any());
    }

    @Test
    public void searchShouldReturnNaverResultsWhenKakaoFails() {

        BlogSearchRequestDTO requestDTO = BlogSearchRequestDTO.builder().query("야구").build();
        when(kakaoWebClient.searchBlog(requestDTO)).thenReturn(Mono.error(new RuntimeException()));

        BlogPostNaverResponseDTO responseDTO = BlogPostNaverResponseDTO.builder().items(List.of(
                BlogPostNaverDTO.builder().postdate("2023-03-21").title("한국 프로야구 개막").description("한국 프로야구 개막 경기 일정...").link("https://blog.naver.com/2344343").build(),
                BlogPostNaverDTO.builder().postdate("2023-03-21").title("잠실 야구장").description("경기 관람 일정....").link("https://blog.naver.com/4355553").build()
        )).build();

        when(naverWebClient.searchBlog(any())).thenReturn(Mono.just(responseDTO));


        BlogSearchResultDTO result = blogSearchService.search(requestDTO).block();

        assertThat(result).isNotNull();
        verify(kakaoWebClient).searchBlog(requestDTO);
        verify(naverWebClient).searchBlog(any());
    }

    @Test
    public void searchShouldThrowExceptionWhenBothClientsFail() {

        BlogSearchRequestDTO requestDTO = BlogSearchRequestDTO.builder().query("손흥민 축구선수").build();
        when(kakaoWebClient.searchBlog(requestDTO)).thenReturn(Mono.error(new RuntimeException()));
        when(naverWebClient.searchBlog(any())).thenReturn(Mono.error(new RuntimeException()));


        assertThatThrownBy(() -> blogSearchService.search(requestDTO).block())
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Failed to retrieve blog posts");
        verify(kakaoWebClient).searchBlog(requestDTO);
        verify(naverWebClient).searchBlog(any());
    }
}

