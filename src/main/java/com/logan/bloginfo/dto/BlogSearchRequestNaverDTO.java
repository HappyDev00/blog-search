package com.logan.bloginfo.dto;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import reactor.util.annotation.Nullable;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;


@Getter
@Setter
@AllArgsConstructor
@Builder
public class BlogSearchRequestNaverDTO {
    @NotNull
    @NotEmpty
    private String query;
    @Nullable
    @Max(1000)
    private Integer start;
    @Nullable
    @Max(100)
    private Integer display;
    @Nullable
    private BlogSortNaverType sort;

}
