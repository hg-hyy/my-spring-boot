package com.hg.hyy.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("/test")
public class TsetController {
    private static final Logger log = LoggerFactory.getLogger(VueController.class);

    @ApiOperation("测试日志")
    @GetMapping("/log")
    public String log() {

        log.error("我是一条error日志");
        return "log is log";
    }

    @ApiOperation("测试异常")
    @SuppressWarnings("unused")
    @GetMapping("/err")
    public String err() {

        try {
            int a = 1 / 0;
        } catch (Exception e) {
            log.error("算术异常", e);
        }
        return "err";
    }

}
