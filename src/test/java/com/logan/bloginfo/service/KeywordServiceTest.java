package com.logan.bloginfo.service;

import com.logan.bloginfo.model.PopularKeyword;
import com.logan.bloginfo.repository.PopularKeywordRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class KeywordServiceTest {

    @Mock
    private PopularKeywordRepository popularKeywordRepository;

    @InjectMocks
    private KeywordService keywordService;

    @Test
    public void testAggregateKeyword() {

        String keyword = "키워드 테스트";
        PopularKeyword mockKeyword = PopularKeyword.builder().keyword(keyword).id(1L).count(1L).build();

        when(popularKeywordRepository.findByKeyword(keyword)).thenReturn(Optional.of(mockKeyword));
        keywordService.aggregateKeyword(keyword);

        verify(popularKeywordRepository, times(1)).save(mockKeyword);
        assertEquals(2L, mockKeyword.getCount());

        reset(popularKeywordRepository);

        when(popularKeywordRepository.findByKeyword(anyString())).thenReturn(Optional.empty());

        String newKeyword = "신규 키워드 테스트";
        PopularKeyword newMockKeyword = PopularKeyword.builder().keyword(newKeyword).count(1L).build();

        when(popularKeywordRepository.save(any())).thenReturn(newMockKeyword);
        keywordService.aggregateKeyword(newKeyword);

        ArgumentCaptor<PopularKeyword> argumentCaptor = ArgumentCaptor.forClass(PopularKeyword.class);
        verify(popularKeywordRepository, times(1)).save(argumentCaptor.capture());
    }

    @Test
    public void testGetTop10Keywords() {
        List<PopularKeyword> mockKeywords = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            PopularKeyword mockKeyword = PopularKeyword.builder().keyword("keyword " + i).count((long) (i + 1)).build();
            mockKeywords.add(mockKeyword);
        }
        when(popularKeywordRepository.findTop10ByOrderByCountDesc()).thenReturn(Optional.of(mockKeywords));

        Optional<List<PopularKeyword>> topKeywordsOptional = keywordService.getTop10Keywords();
        assertTrue(topKeywordsOptional.isPresent());
    }
}

