package com.bsideTinkerbell.wavingBe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
//import org.springframework.context.annotation.PropertySource;

@EnableJpaAuditing
@SpringBootApplication
public class WavingBeApplication {

	public static void main(String[] args) {
		SpringApplication.run(WavingBeApplication.class, args);
	}

}