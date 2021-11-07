package com.hg.hyy.entity;

import java.util.Map;

public class UserInfo {

    private Boolean female;
    private String[] hobbies;
    private Double discount;
    private Integer age;
    private Map<String, Integer> features;

    public Boolean getFemale() {
        return female;
    }

    public void setFemale(Boolean female) {
        this.female = female;
    }

    public String[] getHobbies() {
        return hobbies;
    }

    public void setHobbies(String[] hobbies) {
        this.hobbies = hobbies;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Map<String, Integer> getFeatures() {
        return features;
    }

    public void setFeatures(Map<String, Integer> features) {
        this.features = features;
    }

}
