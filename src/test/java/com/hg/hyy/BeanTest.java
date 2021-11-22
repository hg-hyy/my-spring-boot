package com.hg.hyy;

import com.hg.hyy.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Application.class)
public class BeanTest {

    @Autowired
    User user;

    @Test
    public void beanTest() {
        user.setUsername("fhh");
        System.out.println(user.toString());
    }
}
