package com.logan.bloginfo.service;

import com.logan.bloginfo.model.PopularKeyword;
import com.logan.bloginfo.repository.PopularKeywordRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@AllArgsConstructor
@Service
public class KeywordService {

    private final PopularKeywordRepository popularKeywordRepository;

    @Transactional
    public void aggregateKeyword(String keyword) {
        Optional<PopularKeyword> keywordOptional = popularKeywordRepository.findByKeyword(keyword);

        if (keywordOptional.isPresent()) {
            PopularKeyword popularKeyword = keywordOptional.get();
            popularKeyword.setCount(popularKeyword.getCount() + 1);
            popularKeywordRepository.save(popularKeyword);
        } else {
            popularKeywordRepository.save(PopularKeyword.builder().keyword(keyword).count(1L).build());
        }
    }

    public Optional<List<PopularKeyword>> getTop10Keywords() {
        return popularKeywordRepository.findTop10ByOrderByCountDesc();
    }
}
