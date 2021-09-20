package io.github.thiagocesar1.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class ApiSpringApplication{

	public static void main(String[] args) {
		SpringApplication.run(ApiSpringApplication.class, args);
	}

}
