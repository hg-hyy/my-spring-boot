package com.hg.hyy;

import com.hg.hyy.config.MyConfig;
import com.hg.hyy.entity.Human;
import com.hg.hyy.entity.Pet;
import com.hg.hyy.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@Slf4j
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = Application.class)
public class BeanTest {

  @Autowired User user;

  @Autowired ApplicationContext applicationContext;

  @Test
  public void beanTest() {
    MyConfig bean = applicationContext.getBean(MyConfig.class);
    Human h1 = bean.human();
    Human h2 = bean.human();
    System.out.println(h1 == h2);
    Human fhh = applicationContext.getBean("fhh", Human.class);
    log.error(fhh.toString());
    Pet tom = applicationContext.getBean("tom", Pet.class);
    System.out.println(fhh.getPet() == tom);
    user.setUsername("fhh");
    System.out.println(user.toString());
  }
}
