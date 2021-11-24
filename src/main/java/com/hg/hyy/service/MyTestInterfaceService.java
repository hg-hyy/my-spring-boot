package com.hg.hyy.service;

import com.hg.hyy.interfaces.TestInterface;
import com.hg.hyy.interfaces.impl.TestInterfaceImpl1;
import com.hg.hyy.interfaces.impl.TestInterfaceImpl2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author littl
 * @description 主测试类
 * @email 1021509854@qq.com
 * @date 2021-11-24 09:45:56
 */
@Service
public class MyTestInterfaceService {

  private Logger log = LoggerFactory.getLogger(MyTestInterfaceService.class);
  private TestInterfaceImpl1 testInterfaceImpl1;
  private TestInterfaceImpl2 testInterfaceImpl2;
  private ApplicationContext applicationContext;
  private final List<TestInterface> indexList;
  private Map<String, TestInterface> indexMap;
  private Set<TestInterface> indexSet;

  @Autowired
  public void setIndexMap(Map<String, TestInterface> indexMap) {
    this.indexMap = indexMap;
  }

  @Autowired
  public void setIndexSet(Set<TestInterface> indexSet) {
    this.indexSet = indexSet;
  }

  @Autowired
  public void setTestInterfaceImpl1(TestInterfaceImpl1 testInterfaceImpl1) {
    this.testInterfaceImpl1 = testInterfaceImpl1;
  }

  @Autowired
  public void setTestInterfaceImpl2(TestInterfaceImpl2 testInterfaceImpl2) {
    this.testInterfaceImpl2 = testInterfaceImpl2;
  }

  @Autowired
  public void setApplicationContext(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  public MyTestInterfaceService(List<TestInterface> indexList) {
    this.indexList = indexList;
  }

  public void autowired() {
    testInterfaceImpl1.sayHello();
    testInterfaceImpl2.sayHello();
    log.error(applicationContext.getBean(MyTestInterfaceService.class).toString());
    for (TestInterface s : indexList) {
      s.sayHello();
      log.error(s.toString());
    }

    for (TestInterface s : indexSet) {
      log.error(s.toString());
    }
    for (String s : indexMap.keySet()) {
      log.error(indexMap.get(s).toString());
    }
  }
}
