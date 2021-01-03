package com.wangsheng.springcloud.message.kafka;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.MessageChannel;

public interface MSChannel {

    String LOGIN_CONSUMER = "login_consumer";

    /**
     * 消费登录事件
     * @return
     */
    @Input(LOGIN_CONSUMER)
    MessageChannel loginConsumer();
}
