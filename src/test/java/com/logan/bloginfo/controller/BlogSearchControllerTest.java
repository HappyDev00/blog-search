package com.logan.bloginfo.controller;

import com.logan.bloginfo.dto.BlogSearchRequestDTO;
import com.logan.bloginfo.dto.BlogSearchResultDTO;
import com.logan.bloginfo.model.PopularKeyword;
import com.logan.bloginfo.service.BlogSearchService;
import com.logan.bloginfo.service.KeywordService;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;


public class BlogSearchControllerTest {
    @Test
    public void testSearchBlogWithValidRequest() throws MethodArgumentNotValidException {

        BlogSearchService blogSearchService = mock(BlogSearchService.class);
        KeywordService keywordService = mock(KeywordService.class);

        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);

        BlogSearchRequestDTO request = BlogSearchRequestDTO.builder().query("사과").build();

        BlogSearchResultDTO blogSearchResult = mock(BlogSearchResultDTO.class);
        when(blogSearchService.search(request)).thenReturn(Mono.just(blogSearchResult));

        BlogSearchController controller = new BlogSearchController(blogSearchService, keywordService);

        Mono<BlogSearchResultDTO> result = controller.searchBlog(request, bindingResult);

        StepVerifier.create(result)
                .expectNextCount(1)
                .verifyComplete();

        verify(blogSearchService, times(1)).search(request);
        verify(keywordService, times(1)).aggregateKeyword(request.getQuery());
    }

    @Test
    public void testGetPopularSearches() {

        KeywordService keywordService = mock(KeywordService.class);

        List<PopularKeyword> popularKeywords = Arrays.asList(
                new PopularKeyword(1L, "대중교통마스크", 10L),
                new PopularKeyword(2L, "애플페이", 5L),
                new PopularKeyword(3L, "나폴리", 3L)
        );
        when(keywordService.getTop10Keywords()).thenReturn(Optional.of(popularKeywords));

        BlogSearchController controller = new BlogSearchController(null, keywordService);
        ResponseEntity<List<PopularKeyword>> result = controller.getPopularSearches();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(popularKeywords, result.getBody());
    }

    @Test
    public void testGetPopularSearchesWithEmptyList() {

        KeywordService keywordService = mock(KeywordService.class);

        List<PopularKeyword> popularKeywords = Collections.emptyList();
        when(keywordService.getTop10Keywords()).thenReturn(Optional.of(popularKeywords));

        BlogSearchController controller = new BlogSearchController(null, keywordService);
        try {
            controller.getPopularSearches();
            fail("EntityNotFoundException should have been thrown");
        } catch (EntityNotFoundException e) {
            assertEquals("Popular list not found", e.getMessage());
        }
    }

    @Test
    public void testGetPopularSearchesWithNullList() {

        KeywordService keywordService = mock(KeywordService.class);

        when(keywordService.getTop10Keywords()).thenReturn(Optional.empty());

        BlogSearchController controller = new BlogSearchController(null, keywordService);
        try {
            controller.getPopularSearches();
            fail("EntityNotFoundException should have been thrown");
        } catch (EntityNotFoundException e) {
            assertEquals("Popular list not found", e.getMessage());
        }
    }

}
