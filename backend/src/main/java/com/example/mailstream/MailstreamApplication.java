package com.example.mailstream;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
import java.util.TimeZone;

@EnableKafka
@SpringBootApplication
public class MailstreamApplication {

	public static void main(String[] args) {
		// 1. Force the timezone BEFORE Spring Boot begins its auto-configuration
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Kolkata"));

		// 2. Now it is safe to wake up Spring Boot and Hibernate
		SpringApplication.run(MailstreamApplication.class, args);
	}
}