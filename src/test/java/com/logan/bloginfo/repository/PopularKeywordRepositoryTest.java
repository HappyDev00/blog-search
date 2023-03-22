package com.logan.bloginfo.repository;

import com.logan.bloginfo.model.PopularKeyword;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class PopularKeywordRepositoryTest {

    @Autowired
    private PopularKeywordRepository popularKeywordRepository;

    @Test
    void testSave() {
        PopularKeyword keyword = PopularKeyword.builder()
                .keyword("손흥민")
                .count(1L)
                .build();

        PopularKeyword savedKeyword = popularKeywordRepository.save(keyword);
        assertThat(savedKeyword.getId()).isNotNull();
        assertThat(savedKeyword.getKeyword()).isEqualTo(keyword.getKeyword());
        assertThat(savedKeyword.getCount()).isEqualTo(keyword.getCount());
    }

    @Test
    void testFindByKeyword() {

        String keywordStr1 = "미세먼지";
        String keywordStr2 = "코로나";
        String keywordStr3 = "마스크";

        PopularKeyword keyword1 = PopularKeyword.builder()
                .keyword(keywordStr1)
                .count(1L)
                .build();
        PopularKeyword keyword2 = PopularKeyword.builder()
                .keyword(keywordStr2)
                .count(2L)
                .build();
        popularKeywordRepository.save(keyword1);
        popularKeywordRepository.save(keyword2);

        Optional<PopularKeyword> result1 = popularKeywordRepository.findByKeyword(keywordStr1);
        assertThat(result1).isPresent();
        assertThat(result1.get().getId()).isEqualTo(keyword1.getId());
        assertThat(result1.get().getKeyword()).isEqualTo(keyword1.getKeyword());
        assertThat(result1.get().getCount()).isEqualTo(keyword1.getCount());

        Optional<PopularKeyword> result2 = popularKeywordRepository.findByKeyword(keywordStr2);
        assertThat(result2).isPresent();
        assertThat(result2.get().getId()).isEqualTo(keyword2.getId());
        assertThat(result2.get().getKeyword()).isEqualTo(keyword2.getKeyword());
        assertThat(result2.get().getCount()).isEqualTo(keyword2.getCount());

        Optional<PopularKeyword> result3 = popularKeywordRepository.findByKeyword(keywordStr3);
        assertThat(result3).isEmpty();
    }

    @Test
    void testFindTop10ByOrderByCountDesc() {
        PopularKeyword keyword1 = PopularKeyword.builder()
                .keyword("서울시")
                .count(1L)
                .build();
        PopularKeyword keyword2 = PopularKeyword.builder()
                .keyword("광주광역시")
                .count(2L)
                .build();
        PopularKeyword keyword3 = PopularKeyword.builder()
                .keyword("부산광역시")
                .count(3L)
                .build();
        popularKeywordRepository.save(keyword1);
        popularKeywordRepository.save(keyword2);
        popularKeywordRepository.save(keyword3);

        List<PopularKeyword> result = popularKeywordRepository.findTop10ByOrderByCountDesc().orElse(null);
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(3);
        assertThat(result.get(0).getKeyword()).isEqualTo(keyword3.getKeyword());
        assertThat(result.get(0).getCount()).isEqualTo(keyword3.getCount());
        assertThat(result.get(1).getKeyword()).isEqualTo(keyword2.getKeyword());
        assertThat(result.get(1).getCount()).isEqualTo(keyword2.getCount());
        assertThat(result.get(2).getKeyword()).isEqualTo(keyword1.getKeyword());
        assertThat(result.get(2).getCount()).isEqualTo(keyword1.getCount());
    }
}


