package com.khoi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class EcommerceBeApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcommerceBeApplication.class, args);
	}

}
