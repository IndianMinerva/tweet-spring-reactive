package com.im.penguin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.EnableWebFlux;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebFlux;

@SpringBootApplication
@EnableWebFlux
public class PenguinApplication {

	public static void main(String[] args) {
		SpringApplication.run(PenguinApplication.class, args);
	}

}
