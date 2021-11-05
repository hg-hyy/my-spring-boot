package com.hg.hyy.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 视图映射相关配置
 *
 * @author hyy
 * @date 2021-11-05
 * @since 1.0.0
 */
@Configuration
public class ViewConfig implements WebMvcConfigurer {

    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/v1").setViewName("index");
        registry.addViewController("/home").setViewName("home");
        registry.addViewController("/ws").setViewName("ws");
        registry.addViewController("/hello").setViewName("hello");
        registry.addViewController("/login").setViewName("login");
    }

}
