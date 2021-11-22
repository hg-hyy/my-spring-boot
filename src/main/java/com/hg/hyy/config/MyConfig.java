package com.hg.hyy.config;

import com.hg.hyy.entity.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration(proxyBeanMethods = false)关闭单列，lite模式，
@Configuration
public class MyConfig {

    @Bean
    public User user() {
        return new User();
    }

}
