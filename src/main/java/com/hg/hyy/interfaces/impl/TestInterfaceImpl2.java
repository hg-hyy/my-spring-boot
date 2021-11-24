package com.hg.hyy.interfaces.impl;

import com.hg.hyy.interfaces.TestInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author littl
 * @description 接口实现2
 * @email 1021509854@qq.com
 * @date 2021-11-24 09:36:30
 */
@Slf4j
@Component
public class TestInterfaceImpl2 implements TestInterface {
  @Override
  public void sayHello() {
    log.error("hello,I am TestInterfaceImpl2");
  }
}
