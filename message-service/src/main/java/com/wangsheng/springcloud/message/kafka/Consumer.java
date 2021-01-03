package com.wangsheng.springcloud.message.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class Consumer {
    @StreamListener(MSChannel.LOGIN_CONSUMER)
    public void LOGIN_CONSUMER(Message message, @Header(KafkaHeaders.ACKNOWLEDGMENT) Acknowledgment ack){
            Object msg = message.toString();
            log.info("02-Group1消费了:Topic,Message: " +msg);
            ack.acknowledge();
    }
}
