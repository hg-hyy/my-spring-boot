package com.hg.hyy.vue;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class VueController {

    private String account = "admin";
    private String pwd = "111111";

    @PostMapping("/signin")
    public Msg signin(@RequestBody User user) {

        Msg msg = new Msg("", 0, "");

        if (user.getUsername().equals(account) && user.getPassword().equals(pwd)) {
            msg.setMsg("you are success login!");
            msg.setCode(1000);
            msg.setData("");
        } else {
            msg.setMsg("login failed!");
            msg.setCode(500);
            msg.setData("");
        }

        return msg;
    }

}
