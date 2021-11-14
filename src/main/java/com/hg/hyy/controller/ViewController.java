package com.hg.hyy.controller;

import java.util.concurrent.atomic.AtomicLong;

import com.hg.hyy.entity.Greeting;
import com.hg.hyy.entity.Msg;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.swagger.annotations.ApiOperation;

/**
 * @author zenghui
 * @date 2020-05-20
 */
@Controller
@RequestMapping("/v2")
public class ViewController {

    private static final Logger log = LoggerFactory.getLogger(VueController.class);
    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @ApiOperation("wss")
    @GetMapping("/wss")
    public String wss() {

        return "wss";
    }

    @ApiOperation("测试权限管理")
    @GetMapping("/role")
    public String index(Model model) {
        Msg msg = new Msg("测试标题", 1000, "额外信息，只对管理员显示");
        model.addAttribute("msg", msg);
        return "index";
    }

    @ApiOperation("测试 get传参 model传参到html")
    // You can also add the @CrossOrigin annotation at the controller class level as
    // well, to enable CORS on all handler methods of this class.
    @GetMapping("/greet")
    public String greet(@RequestParam(value = "name", defaultValue = "World") String name, Model model) {
        Greeting g = new Greeting(counter.incrementAndGet(), String.format(template, name));
        model.addAttribute("g", g);
        log.error("hello this is test");
        return "greet";
    }

}