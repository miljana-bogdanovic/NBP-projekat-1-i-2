package com.nbp.timelineservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class TimelineServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TimelineServiceApplication.class, args);
	}

}
