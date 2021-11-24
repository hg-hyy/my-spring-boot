package com.hg.hyy.interfaces.impl;

import com.hg.hyy.entity.User;
import com.hg.hyy.interfaces.TestInterface;
import com.hg.hyy.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author littl
 * @description 接口实现1
 * @email 1021509854@qq.com
 * @date 2021-11-24 09:35:29
 */
@Slf4j
@Component
public class TestInterfaceImpl1 implements TestInterface {

  private UserRepository userRepository;

  @Autowired
  public void setUserRepository(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public void sayHello() {
    Iterable<User> all = userRepository.findAll();
    for (User user : all) {

      String template = "hello,i am %s ";
      log.error(String.format(template, user));
    }
  }
}
