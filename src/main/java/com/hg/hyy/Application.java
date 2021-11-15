package com.hg.hyy;

import com.hg.hyy.entity.Customer;
import com.hg.hyy.entity.CustomerRepository;
import com.hg.hyy.entity.Quote;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@SpringBootApplication
@MapperScan("com.hg.hyy.mapper")
public class Application extends SpringBootServletInitializer {// SpringBootServletInitializer:构建WAR文件并部署，

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


    @Bean
    public CommandLineRunner demo(CustomerRepository repository) {
        return (args) -> {
            // save a few customers
            repository.save(new Customer("Jack", "Bauer"));
            repository.save(new Customer("Chloe", "O'Brian"));
            repository.save(new Customer("Kim", "Bauer"));
            repository.save(new Customer("David", "Palmer"));
            repository.save(new Customer("Michelle", "Dessler"));

            // fetch all customers
            log.info("Customers found with findAll():");
            log.info("-------------------------------");
            for (Customer customer : repository.findAll()) {
                log.info(customer.toString());
            }
            log.info("");

            // fetch an individual customer by ID
            Customer customer = repository.findById(1L);
            log.info("Customer found with findById(1L):");
            log.info("--------------------------------");
            log.info(customer.toString());
            log.info("");

            // fetch customers by last name
            log.info("Customer found with findByLastName('Bauer'):");
            log.info("--------------------------------------------");
            repository.findByLastName("Bauer").forEach(bauer -> log.info(bauer.toString()));
            // for (Customer bauer : repository.findByLastName("Bauer")) {
            //  log.info(bauer.toString());
            // }
            log.info("");
        };
    }
    /*
     * 打印所有spring bean
     *
     * @Bean public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
     * return args -> {
     * System.out.println("Let's inspect the beans provided by Spring Boot:");
     * String[] beanNames = ctx.getBeanDefinitionNames(); Arrays.sort(beanNames);
     * for (String beanName : beanNames) { System.out.println(beanName); } }; }
     */

}
