package com.example.PlayGround;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaAuditing //BasicEntity를 사용하기 위함.
@EnableJpaRepositories(basePackages = "com.example.playground.repository")
public class PlayGroundApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlayGroundApplication.class, args);
	}

}
