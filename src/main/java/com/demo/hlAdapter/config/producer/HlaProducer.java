package com.demo.hlAdapter.config.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HlaProducer {

    @Autowired
    private KafkaTemplate<Object, Object> kafkaTemplate;

    public HlaProducer() {
        log.info("::::::Inside HlaProducer Constructor::::");
    }

    public ListenableFuture<SendResult<Object, Object>> sendMessage(String topic, Object message) {
        ListenableFuture<SendResult<Object, Object>> future = kafkaTemplate.send(topic, message);
        return future;
    }
}
