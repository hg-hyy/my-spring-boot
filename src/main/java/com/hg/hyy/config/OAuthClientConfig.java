package com.hg.hyy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration(proxyBeanMethods = false)
public class OAuthClientConfig {

    // @Bean
    // public SecurityFilterChain securityFilterChain(HttpSecurity http) throws
    // Exception {
    // http.authorizeRequests().anyRequest().authenticated();
    // http.oauth2Login().redirectionEndpoint().baseUri("/v1");
    // return http.build();
    // }

}
