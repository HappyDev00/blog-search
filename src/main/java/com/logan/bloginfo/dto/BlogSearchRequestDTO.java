package com.logan.bloginfo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import reactor.util.annotation.Nullable;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;


@Getter
@Setter
@AllArgsConstructor
@Builder
public class BlogSearchRequestDTO {
    @NotNull
    @NotEmpty
    @ApiParam(example="WBC 야구 경기", required = true)
    private String query;
    @Nullable
    @Min(1)
    @Max(50)
    @ApiParam(value = "1 ~ 50")
    private Integer page;
    @Nullable
    @Min(1)
    @Max(50)
    @ApiParam(value = "1 ~ 50")
    private Integer size;
    @Nullable
    @ApiParam(value = "ACCURACY: 정확도순, RECENCY: 최신순")
    private BlogSortType sort;
}
