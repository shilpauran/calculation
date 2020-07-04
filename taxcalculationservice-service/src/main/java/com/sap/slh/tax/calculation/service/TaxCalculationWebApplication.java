package com.sap.slh.tax.calculation.service;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@ComponentScan("com.sap.slh.tax.*")
@EntityScan("com.sap.slh.tax.*")
@SpringBootApplication
@EnableAsync
@EnableRabbit

public class TaxCalculationWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaxCalculationWebApplication.class, args);
	}

}
