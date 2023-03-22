package com.logan.bloginfo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class BlogInfoApplication {

	public static void main(String[] args) {
		log.info("Server RUN");
		SpringApplication.run(BlogInfoApplication.class, args);
	}

}
