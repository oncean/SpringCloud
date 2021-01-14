package com.wangsheng.springcloud.controller;

import com.wangsheng.springcloud.common.model.result.Result;
import com.wangsheng.springcloud.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @Autowired
    private CustomerService customerService;

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
    @PreAuthorize("hasAuthority('ROLE_admin')")
    public Flux<String> flux2(){
        Flux<String> result = Flux.fromStream(IntStream.range(1,5).mapToObj(i->{
            try {
                TimeUnit.SECONDS.sleep(i);
            }catch (InterruptedException e){}
            return "Flux data --" + i;
        }));
        return result;
    }

    @GetMapping(value = "3")
    @PreAuthorize("hasRole('ROLE_admin')")
    public Flux<String> flux3(){
        Flux<String> result = Flux.fromStream(IntStream.range(1,5).mapToObj(i->{
            try {
                TimeUnit.SECONDS.sleep(i);
            }catch (InterruptedException e){}
            return "Flux data --" + i;
        }));
        return result;
    }



    @GetMapping(value = "4")
    @PreAuthorize("hasRole('admin')")
    public Flux<String> flux4(){
        Flux<String> result = Flux.fromStream(IntStream.range(1,5).mapToObj(i->{
            try {
                TimeUnit.SECONDS.sleep(i);
            }catch (InterruptedException e){}
            return "Flux data --" + i;
        }));
        return result;
    }
    @GetMapping(value = "5")
    public Result test(){
        customerService.getResult();
        return Result.success();
    }
    @GetMapping(value = "6")
    public Result test6(){
        customerService.setResult();
        return Result.success();
    }
}
