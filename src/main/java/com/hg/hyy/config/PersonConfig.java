package com.hg.hyy.config;

import java.util.*;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ConfigurationProperties(prefix = "data")
// 如果只有一个主配置类文件，@PropertySource可以不写
@PropertySource("classpath:application.properties")
public class PersonConfig {

    private String lastName;
    private Integer age;
    private Boolean boss;

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Boolean getBoss() {
        return boss;
    }

    public void setBoss(Boolean boss) {
        this.boss = boss;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    private Date birth;

    private Map<String, String> person;
    private List<String> list;

    public Map<String, String> getPerson() {
        return person;
    }

    public void setPerson(Map<String, String> person) {
        this.person = person;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }
}
