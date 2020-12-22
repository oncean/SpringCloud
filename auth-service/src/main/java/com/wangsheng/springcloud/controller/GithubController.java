package com.wangsheng.springcloud.controller;

import com.wangsheng.springcloud.common.model.result.Result;
import com.wangsheng.springcloud.service.GithubService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gitHub")
@Slf4j
public class GithubController {

    @Autowired
    private GithubService githubService;

    @GetMapping("/callback")
    public Result callBack(@RequestParam("code") String code) {
        log.info("get token ");
        String token = githubService.loginGithub(code);
        log.info("get token success"+ token);
        return Result.success(token);
    }

    @GetMapping("/getClientId")
    public Result getClientId(){
        return Result.success(githubService.getClientId());
    }
}
