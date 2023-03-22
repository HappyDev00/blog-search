package com.logan.bloginfo.dto;

import io.swagger.annotations.ApiModel;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class BlogSearchResultDTO {

    private List<BlogPostDTO> documents;

}
