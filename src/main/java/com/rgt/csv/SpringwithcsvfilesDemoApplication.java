package com.rgt.csv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;


@SpringBootApplication
public class SpringwithcsvfilesDemoApplication extends SpringBootServletInitializer{
@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder initializer) {
	  return initializer.sources(SpringwithcsvfilesDemoApplication.class);
		
	}
	public static void main(String[] args) {
		SpringApplication.run(SpringwithcsvfilesDemoApplication.class, args);
	}

}
