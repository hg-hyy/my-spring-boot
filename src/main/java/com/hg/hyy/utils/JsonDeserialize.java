package com.hg.hyy.utils;

import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hg.hyy.kafka.KafkaData;

import org.apache.kafka.common.serialization.Deserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonDeserialize implements Deserializer<Object> {

    private ObjectMapper mapper;
    private final Logger log = LoggerFactory.getLogger(this.getClass().getName());

    @Override
    public void configure(Map<String, ?> map, boolean b) {
        mapper = new ObjectMapper();
    }

    @Override
    public Object deserialize(String topic, byte[] data) {
        KafkaData kd = null;
        
        try {
            kd = mapper.readValue(data, KafkaData.class);

        } catch (Exception e) {
            log.error("Deserializer error:", e);
        }
        return kd;
    }


    // public Person deserialize(String topic, byte[] data) {
    //     return ProtostuffUtil.deserializer(data, Person.class);
    // }
    
    @Override
    public void close() {
    }
}
