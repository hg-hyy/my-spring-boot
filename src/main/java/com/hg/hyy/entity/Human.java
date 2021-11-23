package com.hg.hyy.entity;

/**
 * @author littl
 * @description 人类
 * @email 1021509854@qq.com
 * @date 2021-11-23 16:49:38
 */
public class Human {
  public String name;
  public Integer age;
  private Pet pet;

  public Pet getPet() {
    return pet;
  }

  public void setPet(Pet pet) {
    this.pet = pet;
  }

  public Human(String name, Integer age) {
    this.name = name;
    this.age = age;
  }

  @Override
  public String toString() {
    return "Human{" + "name='" + name + '\'' + ", age=" + age + ", pet=" + pet + '}';
  }
}
