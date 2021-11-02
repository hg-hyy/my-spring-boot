package com.hg.hyy.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ViewConfig implements WebMvcConfigurer {

    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/v1/").setViewName("index");
        registry.addViewController("/v1/home").setViewName("home");
        registry.addViewController("/v1/hello").setViewName("hello");
        registry.addViewController("/v1/login").setViewName("login");
    }

}
