package com.hg.hyy.config;

import com.hg.hyy.entity.Human;
import com.hg.hyy.entity.Pet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// @Configuration(proxyBeanMethods = false)关闭full模式，lite模式，
@Configuration
public class MyConfig {

  @Bean("fhh")
  public Human human() {
    Human fhh = new Human("fhh", 35);
    fhh.setPet(pet());
    return fhh;
  }

  @Bean("tom")
  public Pet pet() {
    return new Pet("tom");
  }
}
