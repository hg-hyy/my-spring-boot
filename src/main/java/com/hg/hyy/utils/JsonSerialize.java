package com.hg.hyy.utils;

import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonSerialize implements Serializer<Object> {

    private final Logger log = LoggerFactory.getLogger(this.getClass().getName());

    private String encoding = "UTF8";
    private ObjectMapper mapper;

    @Override
    public void configure(Map<String, ?> map, boolean b) {
        mapper = new ObjectMapper();
    }

    // public byte[] serialize(String s, Object o) {
    // try {
    // ByteArrayOutputStream baos = new ByteArrayOutputStream();
    // ObjectOutputStream oos = new ObjectOutputStream(baos);
    // oos.writeObject(o);
    // oos.close();
    // byte[] b = baos.toByteArray();
    // return b;
    // } catch (IOException e) {
    // return new byte[0];
    // }
    // }
    
    @Override
    public byte[] serialize(String s, Object data) {
        try {
            return mapper.writeValueAsString(data).getBytes(encoding);
        } catch (Exception e) {
            log.error("Serializer error:", e);
        }
        return null;
    }

    // public byte[] serialize(String topic, Person data) {
    //     return ProtostuffUtil.serializer(data, Person.class);
    // }


    @Override
    public void close() {

    }

}
