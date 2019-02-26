package br.com.zup.poc.demospringboot2resilience4j;

import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.circuitbreaker.monitoring.health.CircuitBreakerHealthIndicator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableConfigurationProperties
@EnableFeignClients
public class DemoSpringBoot2Resilience4jApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoSpringBoot2Resilience4jApplication.class, args);
	}

	@Bean
	public HealthIndicator drinkApp(CircuitBreakerRegistry circuitBreakerRegistry){
		return new CircuitBreakerHealthIndicator(circuitBreakerRegistry.circuitBreaker("drinkApp"));
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

}

