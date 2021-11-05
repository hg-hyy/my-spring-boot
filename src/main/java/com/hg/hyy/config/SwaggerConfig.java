package com.hg.hyy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger相关配置
 *
 * @author hyy
 * @date 2021-11-05
 * @since 1.0.0
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
        @Bean
        public Docket createRestApi() {
                return new Docket(DocumentationType.SWAGGER_2).pathMapping("/").select()
                                .apis(RequestHandlerSelectors.basePackage("com.hg.hyy")).paths(PathSelectors.any())
                                .build()
                                .apiInfo(new ApiInfoBuilder().title("SpringBoot_Swagger").description("详细信息")
                                                .version("1.0")
                                                .contact(new Contact("hyy", "https://www.hg-hyy.com",
                                                                "littleshenyun@outlook.com"))
                                                .license("The Apache License")
                                                .licenseUrl("https://github.com/shenyunbrother?tab=repositories")
                                                .build());
        }
}
