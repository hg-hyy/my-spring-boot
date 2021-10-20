package com.hg.hyy.kafka;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/log")
public class IndexController {
    private static final Logger log = LoggerFactory.getLogger(IndexController.class);

    @GetMapping("/index")
    public String index() {
        InputMDC.putMDC();
        log.error("我是一条error日志");
        return "log is log";
    }

    @GetMapping("/err")
    public String err() {
        InputMDC.putMDC();
        try {
            int a = 1 / 0;
        } catch (Exception e) {
            log.error("算术异常", e);
        }
        return "err";
    }

}
