package com.sandy.ecp.gateway.startup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication(exclude = {
	SecurityAutoConfiguration.class
})
public class EcpGatewayApplication {

	public static void main(String[] args) {
		
		SpringApplication.run(EcpGatewayApplication.class, args);
	}
}
