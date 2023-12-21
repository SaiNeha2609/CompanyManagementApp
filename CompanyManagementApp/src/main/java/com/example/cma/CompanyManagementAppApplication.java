package com.example.cma;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;


@SpringBootApplication
//@EntityScan("com.example.cma.Entity")
@EnableFeignClients
@ImportAutoConfiguration({FeignAutoConfiguration.class})
//@EnableDiscoveryClient
public class CompanyManagementAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(CompanyManagementAppApplication.class, args);
	}

}
