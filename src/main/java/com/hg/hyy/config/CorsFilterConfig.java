package com.hg.hyy.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.hg.hyy.filters.UrlFilter;

/**
 * CorsFilter相关配置
 *
 * @author hyy
 * @date 2021-11-05
 * @since 1.0.0
 */
@Configuration
public class CorsFilterConfig {

    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost:8090");
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
    public FilterRegistrationBean<UrlFilter> urlFilter() {
        // 通过FilterRegistrationBean实例设置优先级可以生效
        // 通过@WebFilter无效
        FilterRegistrationBean<UrlFilter> bean = new FilterRegistrationBean<UrlFilter>();
        bean.setFilter(new UrlFilter());// 注册自定义过滤器
        bean.setName("urlFilter");// 过滤器名称
        bean.addUrlPatterns("/*");// 过滤所有路径
        bean.setOrder(1);// 优先级，最顶级
        return bean;
    }
}
