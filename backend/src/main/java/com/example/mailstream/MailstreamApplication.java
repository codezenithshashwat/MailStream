package com.example.mailstream;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@SpringBootApplication
public class  MailstreamApplication {

	public static void main(String[] args) {
		SpringApplication.run(MailstreamApplication.class, args);
	}

}
