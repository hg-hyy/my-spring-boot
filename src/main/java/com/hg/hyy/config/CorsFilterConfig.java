package com.hg.hyy.config;

import com.hg.hyy.utils.TestFilter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsFilterConfig {

    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost:8080");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        // config.addAllowedMethod("GET,POST");
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<CorsFilter>(new CorsFilter(source));
        bean.setOrder(0);
        // bean.addInitParameter("cors.supportedMethods", "GET,POST");
        return bean;
    }

    @Bean
    public FilterRegistrationBean<TestFilter> RegistTest1() {
        // 通过FilterRegistrationBean实例设置优先级可以生效
        // 通过@WebFilter无效
        FilterRegistrationBean<TestFilter> bean = new FilterRegistrationBean<TestFilter>();
        bean.setFilter(new TestFilter());// 注册自定义过滤器
        bean.setName("flilter1");// 过滤器名称
        bean.addUrlPatterns("/*");// 过滤所有路径
        bean.setOrder(1);// 优先级，最顶级
        return bean;
    }
}
