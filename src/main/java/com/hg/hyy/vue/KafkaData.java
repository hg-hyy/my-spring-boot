package com.hg.hyy.vue;

import java.util.List;
import java.util.Map;

public class KafkaData {
    private List<Map<String, String>> listMap;
    private int code = 0;

    public KafkaData() {
    }

    public KafkaData(int code) {
        this.code = code;
    }

    public KafkaData(List<Map<String, String>> listMap, int code) {
        this.listMap = listMap;
        this.code = code;
    }

    public List<Map<String, String>> getListMap() {
        return listMap;
    }

    public void setListMap(List<Map<String, String>> listMap) {
        this.listMap = listMap;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "KafkaData{" +
                "listMap=" + listMap +
                ", code=" + code +
                '}';
    }

}
