package com.logan.bloginfo.dto;

import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BlogPostDTO {
    private String datetime;
    private String contents;
    private String title;
    private String url;

}
