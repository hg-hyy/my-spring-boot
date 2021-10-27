package com.hg.hyy.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.time.Duration;
import java.util.*;

/**
 * @author hyy
 * @since 20211020 10:30
 */
public class KafkaClient implements Runnable {

    public static KafkaConsumer<String, Object> createConsumer(String servers, String topic) {
        Properties properties = new Properties();

        // 主机信息
        properties.put("bootstrap.servers", servers);
        // 群组id
        properties.put("group.id", "group-3");
        /**
         * 消费者是否自动提交偏移量，默认是true 为了尽量避免重复数据和数据丢失，可以把它设为true, 由自己控制核实提交偏移量。
         * 如果设置为true,可以通过auto.commit.interval.ms属性来设置提交频率
         */
        properties.put("enable.auto.commit", "true");
        /**
         * 自动提交偏移量的提交频率
         */
        properties.put("auto.commit.interval.ms", "1000");
        /**
         * max.poll.interval.ms参数用于指定consumer两次poll的最大时间间隔（默认5分钟)
         * 如果超过了该间隔consumer client会主动向coordinator发起LeaveGroup请求，触发rebalance；然后consumer重新发送JoinGroup请求
         * （1）实际应用中，消费到的数据处理时长不宜超过max.poll.interval.ms，否则会触发rebalance
         * （2）如果处理消费到的数据耗时，可以尝试通过减小max.poll.records的方式减小单次拉取的记录数（默认是500条）
         */
        properties.put("max.poll.interval.ms", "1000");//默认300000 5分钟。
        properties.put("max.poll.records", "100");
        /**
         * 默认值latest. latest:在偏移量无效的情况下，消费者将从最新的记录开始读取数据
         * erliest:偏移量无效的情况下，消费者将从起始位置读取分区的记录。
         */
        properties.put("auto.offset.reset", "earliest");
        /**
         * 消费者在指定的时间内没有发送心跳给群组协调器，就被认为已经死亡， 协调器就会触发再均衡，把它的分区分配给其他消费者。
         */
        properties.put("session.timeout.ms", "60000");
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        // properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "com.hg.hyy.seranddeser.JsonDeserialize");


        KafkaConsumer<String, Object> kafkaConsumer = new KafkaConsumer<String, Object>(properties);
        /**
         * 订阅主题，这个地方只传了一个主题：topic. 这个地方也可以有正则表达式。
         */
        kafkaConsumer.subscribe(Arrays.asList(topic));
        return kafkaConsumer;
    }

    public static String readMessage(KafkaConsumer<String, Object> kafkaConsumer, Duration timeout) {
        // 无限循环轮询
        while (true) {
            /**
             * 消费者必须持续对Kafka进行轮询，否则会被认为已经死亡，他的分区会被移交给群组里的其他消费者。
             * poll返回一个记录列表，每个记录包含了记录所属主题的信息， 记录所在分区的信息，记录在分区里的偏移量，以及键值对。
             * poll需要一个指定的超时参数，指定了方法在多久后可以返回。 发送心跳的频率，告诉群组协调器自己还活着。
             */
            ConsumerRecords<String, Object> records = kafkaConsumer.poll(timeout);
            StringBuilder sb = new StringBuilder();
            for (ConsumerRecord<String, Object> record : records) {
                Object value = record.value();
                sb.append(value);
                kafkaConsumer.commitAsync();
                // Thread.sleep(1000);
            }
            return sb.toString();
        }
    }

    public static KafkaProducer<String, Object> createProducer(String servers) {
        Properties properties = new Properties();
        // kafkaProps.put("bootstrap.servers","192.168.123.128:9092,192.168.123.129:9092,192.168.123.130:9092");
        // 主机信息（broker）
        properties.put("bootstrap.servers", servers);
        /**
         * acks指定了必须要有多少个分区副本收到消息，生产者才会认为消息写入成功。
         * acks=0,生产者在写入消息之前不会等待任何来自服务器的响应；就算发送失败了，生产者也不知道。
         * acks=1,只要集群首领收到消息，生产者就会收到一个来自服务器的成功消息
         * acks=all，所有参与复制的节点都收到消息，生产者才会收到一个来自服务器的成功响应。
         */
        properties.put("acks", "all");
        /**
         * 发送失败后重发的次数，最终还不成功表示发送彻底的失败
         */
        properties.put("retries", 0);
        /**
         * 默认情况下，消息发送时不会被压缩。 snappy:压缩算法由Google发明，它占用较少的CPU,却能提供较好的性能和相当可观的压缩比
         * gzip:占用较多的CPU,但是提供更高的压缩比，带宽比较有限，可以考虑这个压缩算法。
         * 使用压缩可以降低网络传输开销和存储开销，而这往往时向kafka发送消息的瓶颈
         */
        // properties.put("compression.type", "snappy");

        /**
         * 一个批次可以使用的内存大小；当批次被填满，批次里的所有消息会被发送；不过生产者并不一定等批次被填满才发送；
         * 所以批次大小设置得很大，也不会造成延迟，只是会占用更多得内存而已。但是设置得太小， 因为生产者需要更频繁的发送消息，会增加额外的开销。
         */
        properties.put("batch.size", 16384);// 16KB
        /**
         * 指定了生产者在发送批次之前等待更多消息加入批次的时间。 KafkaProducer会在批次填满或liner.ms达到上限时把批次发送出去。
         * 这样做虽然会出现一些延时，但是会提高吞吐量。
         */
        properties.put("linger.ms", 1);
        /**
         * 生产者内存缓冲区的大小，生产者用它缓冲要发送到服务器的消息。 如果应用程序发送消息的速度超过发送到服务器的速度，会导致生产者空间不足，
         * 这个时候send()方法要么被阻塞，要么抛出异常。
         */
        properties.put("buffer.memory", 33554432);// 32MB
        /**
         * 生产者在收到服务器响应之前可以发送多少个消息。 值越高就会占用越多的内存，不过也会提升吞吐量。
         * 设为1可以保证消息是按照发送顺序填写入服务器的，即使发生了重试。
         */
        properties.put("max.in.flight.requests.per.connection", 1);
        // 键为字符串类型
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        // 值为字符串类型
        // properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "com.hg.hyy.seranddeser.JsonSerialize");
        return new KafkaProducer<String, Object>(properties);
    }

    public static void send(KafkaProducer<String, Object> producer, String topic, Object message) {
        producer.send(new ProducerRecord<String, Object>(topic, message));
    }

    @Override
    public void run() {

        System.out.println("hello,kafka "+Thread.currentThread().getName());
    }
}