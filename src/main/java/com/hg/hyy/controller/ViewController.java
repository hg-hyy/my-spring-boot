package com.hg.hyy.controller;

import com.hg.hyy.entity.Msg;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author zenghui
 * @date 2020-05-20
 */
@Controller
@RequestMapping("/v2")
public class ViewController {
    @GetMapping("/")
    public String home() {
        return "v2home";
    }

    @GetMapping("/login")
    public String login() {
        return "v2login";
    }

    @GetMapping("/index")
    public String index(Model model) {
        Msg msg = new Msg("测试标题", 1000, "额外信息，只对管理员显示");
        model.addAttribute("msg", msg);
        return "index";
    }

}