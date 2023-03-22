package com.logan.bloginfo.exception;

import io.swagger.annotations.ApiModel;
import lombok.*;

import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
public class ErrorMessage {
    private int statusCode;
    private Date timestamp;
    private String message;


}
