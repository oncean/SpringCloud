package com.wangsheng.springcloud.message;

import com.wangsheng.springcloud.message.kafka.MSChannel;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.stream.annotation.EnableBinding;

@SpringBootApplication
@EnableDiscoveryClient
@EnableBinding(MSChannel.class)
public class MessageApplication {
    public static void main(String[] args) {
        SpringApplication.run(MessageApplication.class,args);
    }
}
