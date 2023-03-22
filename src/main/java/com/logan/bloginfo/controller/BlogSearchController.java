package com.logan.bloginfo.controller;

import com.logan.bloginfo.dto.BlogSearchRequestDTO;
import com.logan.bloginfo.dto.BlogSearchResultDTO;
import com.logan.bloginfo.exception.ErrorMessage;
import com.logan.bloginfo.model.PopularKeyword;
import com.logan.bloginfo.service.BlogSearchService;
import com.logan.bloginfo.service.KeywordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Api(value = "Blog Search API")
@RequiredArgsConstructor
@Slf4j
@RestController
public class BlogSearchController {
    private final BlogSearchService blogSearchService;
    private final KeywordService keywordService;
    // 블로그 검색 엔드포인트
    @ApiOperation(value = "블로그 검색", notes = "Keyword를 이용하여 블로그 검색을 수행합니다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Request Success.", response = BlogSearchResultDTO.class),
            @ApiResponse(code = 400, message = "Invalid Parameter.", response = ErrorMessage.class),
            @ApiResponse(code = 404, message = "Keyword not found.", response = ErrorMessage.class),
            @ApiResponse(code = 500, message = "Internal Server error.", response = ErrorMessage.class)
    })
    @GetMapping("/search")
    public  Mono<BlogSearchResultDTO> searchBlog(@Valid BlogSearchRequestDTO blogSearchRequestDTO, BindingResult bindingResult) throws MethodArgumentNotValidException {

        log.info("블로그 검색 요청");
        if(bindingResult.hasErrors()) {
            throw new MethodArgumentNotValidException(null, bindingResult);
        }

        return blogSearchService.search(blogSearchRequestDTO)
                        .doOnNext(blogSearchResultDTO -> {
                            log.debug("blogsearch success...keyword logging");
                            keywordService.aggregateKeyword(blogSearchRequestDTO.getQuery());

                        });

    }

    // 인기 검색어 목록 엔드포인트
    @ApiOperation(value = "인기 검색어 목록 TOP10", notes = "인기 검색어 최대 10개까지 조회합니다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Request Success.", response = PopularKeyword.class),
            @ApiResponse(code = 400, message = "Invalid Parameter.", response = ErrorMessage.class),
            @ApiResponse(code = 404, message = "Popular list not found.", response = ErrorMessage.class),
            @ApiResponse(code = 500, message = "Internal Server error.", response = ErrorMessage.class)
    })
    @GetMapping("/popular-searches")
    public ResponseEntity<List<PopularKeyword>> getPopularSearches() {
        log.info("인기 검색어 목록 요청");
        Optional<List<PopularKeyword>> popularKeywords = keywordService.getTop10Keywords();
        List<PopularKeyword> popularKeywords1 = null;
        if(popularKeywords.isEmpty()) {
            throw new EntityNotFoundException("Popular list not found");
        }
        else {
            popularKeywords1 = popularKeywords.get();
            if (popularKeywords1.size() == 0)
                throw new EntityNotFoundException("Popular list not found");
        }

        return ResponseEntity.ok(popularKeywords1);
    }
}
