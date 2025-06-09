package ru.ya;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class FrontUiApplication {
	public static void main(String[] args) {
		SpringApplication.run(FrontUiApplication.class, args);

	}

}
