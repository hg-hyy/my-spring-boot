package com.hg.hyy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author zenghui
 * @date 2020-05-20
 */
@Controller
@RequestMapping("/v2")
public class LoginController {
    @RequestMapping("/")
    public String home() {
        return "v2home";
    }

    @RequestMapping("/login")
    public String login() {
        return "v2login";
    }
}