package com.se.jewelryauction;

import com.se.jewelryauction.components.configurations.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class JewelryauctionApplication {

	public static void main(String[] args) {
		SpringApplication.run(JewelryauctionApplication.class, args);
	}

}
