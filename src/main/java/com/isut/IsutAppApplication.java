package com.isut;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.isut.property.FileStorageProperties;

@SpringBootApplication
@ComponentScan(basePackages = { "com.isut" })
@EnableConfigurationProperties({ FileStorageProperties.class })
@EnableScheduling
public class IsutAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(IsutAppApplication.class, args);
	}

}
