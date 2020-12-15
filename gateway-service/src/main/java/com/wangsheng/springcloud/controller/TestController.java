package com.wangsheng.springcloud.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@RestController
@RequestMapping("test")
@Slf4j
public class TestController {
    @GetMapping(value = "1", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> flux(){
        Flux<String> result = Flux.fromStream(IntStream.range(1,5).mapToObj(i->{
            try {
                TimeUnit.SECONDS.sleep(i);
            }catch (InterruptedException e){}
            return "Flux data --" + i;
        }));
        return result;
    }

    @GetMapping(value = "2")
    public Flux<String> flux2(){
        Flux<String> result = Flux.fromStream(IntStream.range(1,5).mapToObj(i->{
            try {
                TimeUnit.SECONDS.sleep(i);
            }catch (InterruptedException e){}
            return "Flux data --" + i;
        }));
        return result;
    }
}
