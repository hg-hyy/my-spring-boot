package com.hg.hyy;

import com.hg.hyy.entity.Quote;
import com.hg.hyy.interfaces.StorageService;
import com.hg.hyy.properties.StorageProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import java.io.IOException;
import java.util.Arrays;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
@MapperScan("com.hg.hyy.mapper")
public class Application extends SpringBootServletInitializer { // SpringBootServletInitializer:构建WAR文件并部署，

  private static final Logger log = LoggerFactory.getLogger(Application.class);

  private static SpringApplicationBuilder customizerBuilder(SpringApplicationBuilder builder) {
    // 关闭logo
    return builder.sources(Application.class).bannerMode(Banner.Mode.OFF);
  }

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
    return customizerBuilder(builder);
  }

  public static void main(String[] args) {

    // SpringApplication.run(Application.class, args);

    // SpringApplication application = new SpringApplication(MyApplication.class);
    // application.setBannerMode(Banner.Mode.OFF);
    // application.run(args);

    // new SpringApplicationBuilder()
    // .bannerMode(Banner.Mode.OFF)
    // .sources(Application.class)
    // .run(args);
    try {
      Runtime.getRuntime().exec("src\\main\\resources\\mysqld.bat");
      log.error("mysql start sucess");
    } catch (IOException e) {
      e.printStackTrace();
    }
    customizerBuilder(new SpringApplicationBuilder()).run(args);
  }

  // websocket
  @Bean
  public ServerEndpointExporter serverEndpointExporter() {
    return new ServerEndpointExporter();
  }

  @Bean
  public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer() {
      @Override
      public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/v1/*").allowedOrigins("http://localhost:8090").allowedMethods("POST", "GET")
            .allowedHeaders("*").exposedHeaders("*").allowCredentials(true).maxAge(3600);

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
      assert quote != null;
      log.error("应用启动获取资源成功：" + quote.getValue().getQuote());
    };
  }

  /*
   * 打印所有spring bean
   */
  @Bean
  public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
    return args -> {
      log.error("Let's inspect the beans provided by Spring Boot:");
      String[] beanNames = ctx.getBeanDefinitionNames();
      Arrays.sort(beanNames);
      for (String beanName : beanNames) {
        System.out.println(beanName);
      }
    };
  }

  @Bean
  CommandLineRunner init(StorageService storageService) {
    return (args) -> {
      storageService.deleteAll();
      storageService.init();
    };
  }
}
