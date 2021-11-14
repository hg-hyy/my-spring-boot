package com.hg.hyy.service;

import com.hg.hyy.entity.Greeting;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MyService {

    private final RestTemplate restTemplate;

    public MyService(RestTemplateBuilder restTemplateBuilder) {
        restTemplateBuilder.basicAuthentication("user", "password").build();
        this.restTemplate = restTemplateBuilder.build();
    }

    public Greeting someRestCall(String name) {
        return this.restTemplate.getForObject("/{name}/details", Greeting.class, name);
    }

}
