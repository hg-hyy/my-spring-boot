package com.hg.hyy.entity;

/**
 * @author littl
 * @description 宠物测试类
 * @email 1021509854@qq.com
 * @date 2021-11-23 16:40:38
 */
public class Pet {
  public String name;

  public Pet(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return "Pet{" + "name='" + name + '\'' + '}';
  }
}
