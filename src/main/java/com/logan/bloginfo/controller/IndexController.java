package com.logan.bloginfo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestController
public class IndexController {
    @Value("${server.version}")
    String serverVersion;

    @GetMapping("/")
    @ApiIgnore
    String health(HttpServletRequest request) {
        String message = "Hello, Blog-Keyword Search Server is running...version: " + serverVersion;
        return message;
    }
}
