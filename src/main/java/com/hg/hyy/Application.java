package com.hg.hyy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import com.hg.hyy.entity.Quote;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@SpringBootApplication
public class Application extends SpringBootServletInitializer {
	// 注意，如果您正在构建WAR文件并部署它，则需要WebApplicationInitializer。如果你喜欢运行一个嵌入式Web服务器，那么你根本不需要这个。

	private static final Logger log = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {
		// 关闭logo
		SpringApplication sa = new SpringApplication(Application.class);
		sa.setBannerMode(Banner.Mode.OFF);
		sa.run(args);
		// SpringApplication.run(Application.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(Application.class);
	}

	@Bean
	public ServerEndpointExporter serverEndpointExporter() {
		return new ServerEndpointExporter();
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/spring.ws").allowedOrigins("http://localhost:8090");
			}
		};
	}

	@Bean
	public WebMvcConfigurer corsConfigurer1() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/v1/**").allowedOrigins("http://localhost:8080").allowedMethods("POST", "GET")
						.allowedHeaders("header1", "header2", "header3").exposedHeaders("header1", "header2")
						.allowCredentials(false).maxAge(3600);
			}
		};
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

	@Bean
	public CommandLineRunner run(RestTemplate restTemplate) throws Exception {
		return args -> {
			Quote quote = restTemplate.getForObject("https://quoters.apps.pcfone.io/api/random", Quote.class);
			log.error(quote.toString());
		};
	}
	// 打印所有spring bean
	// @Bean
	// public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
	// return args -> {

	// System.out.println("Let's inspect the beans provided by Spring Boot:");

	// String[] beanNames = ctx.getBeanDefinitionNames();
	// Arrays.sort(beanNames);
	// for (String beanName : beanNames) {
	// System.out.println(beanName);
	// }

	// };
	// }
}
