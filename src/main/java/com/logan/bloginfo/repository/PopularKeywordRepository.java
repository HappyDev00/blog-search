package com.logan.bloginfo.repository;

import com.logan.bloginfo.model.PopularKeyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PopularKeywordRepository extends JpaRepository<PopularKeyword, Long> {

    Optional<PopularKeyword> findByKeyword(String keyword);
    Optional<List<PopularKeyword>> findTop10ByOrderByCountDesc();

}
