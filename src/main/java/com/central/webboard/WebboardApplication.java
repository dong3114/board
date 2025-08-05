package com.central.webboard;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.central.webboard.**.mapper")
public class WebboardApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebboardApplication.class, args);
	}

}
