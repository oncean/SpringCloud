package com.wangsheng.springcloud.controller;

import com.alibaba.nacos.client.config.impl.HttpSimpleClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@RestController
@RequestMapping("auth")
@Slf4j
public class LoginController {
    @GetMapping(value = "test",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> flux(){
        Flux<String> result = Flux.fromStream(IntStream.range(1,5).mapToObj(i->{
           try {
               TimeUnit.SECONDS.sleep(i);
           }catch (InterruptedException e){}
           return "Flux data --" + i;
        }));
        return result;
    }
}
