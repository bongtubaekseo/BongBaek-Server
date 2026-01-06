package org.appjam.bongbaek;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class BongbaekApplication {

	public static void main(String[] args) {
		SpringApplication.run(BongbaekApplication.class, args);
	}
}
