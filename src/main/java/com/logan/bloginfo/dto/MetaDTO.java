package com.logan.bloginfo.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class MetaDTO {
    private int total_count;
    private int pageable_count;
    private boolean is_end;

}
