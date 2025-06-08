package ru.ya;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class FrontUiApplication {
	@Value("${greeting.message}")
	private static String message;

	public static void main(String[] args) {
		SpringApplication.run(FrontUiApplication.class, args);
		System.out.println("greeting message: " + message);

	}

}
