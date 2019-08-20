package com.inventory;

import com.inventory.config.ApplicationConfigurationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.io.IOException;

@SpringBootApplication
@EnableConfigurationProperties(ApplicationConfigurationProperties.class)
@EnableJpaAuditing
public class AmronApplication {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(AmronApplication.class);
		openHomePage();
	}

	private static void openHomePage() throws IOException {
		Runtime rt = Runtime.getRuntime();
		rt.exec("rundll32 url.dll,FileProtocolHandler " + "http://localhost:8080");
	}
}
