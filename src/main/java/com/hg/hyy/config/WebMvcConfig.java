package com.hg.hyy.config;

import com.hg.hyy.interceptors.CustomHandlerInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

/**
 * 视图映射相关配置
 *
 * @author hyy
 * @since 2021-11-05
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

  /** 配置ViewController */
  public void addViewControllers(ViewControllerRegistry registry) {
    registry.addViewController("/home").setViewName("home");
    registry.addViewController("/wss").setViewName("wss");
    registry.addViewController("/hello").setViewName("hello");
    registry.addViewController("/login").setViewName("login");
    registry.addViewController("/results").setViewName("results");
  }

  /** 配置ResourceHandlers 处理静态资源的，例如：图片，js，css等 */
  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
    registry
        .addResourceHandler("swagger-ui.html")
        .addResourceLocations("classpath:/META-INF/resources/");
    registry
        .addResourceHandler("/webjars/**")
        .addResourceLocations("classpath:/META-INF/resources/webjars/");
  }

  /** 配置CORS跨域 */
  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry
        .addMapping("/v1/*")
        .allowedOrigins("http://localhost:8090")
        .allowedMethods("POST", "GET")
        .allowedHeaders("*")
        .exposedHeaders("*")
        .allowCredentials(true)
        .maxAge(3600);
  }

  /** 拦截器配置 */
  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(new CustomHandlerInterceptor()).addPathPatterns("/v2/role");
  }
}
