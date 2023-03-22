package com.logan.bloginfo.dto;

import lombok.*;

import java.util.List;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BlogPostNaverResponseDTO {
    private String lastBuildDate;
    private int total;
    private int start;
    private int display;
    private List<BlogPostNaverDTO> items;

}
