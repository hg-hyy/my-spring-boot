package com.hg.hyy.vue;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class VueController {

    private String account = "admin";
    private String pwd = "111111";
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/signin")
    public Msg signin(@RequestBody User user) {

        Msg msg = new Msg("", 0, "");
        if (userRepository.findAll().iterator().next().getUsername().equals(user.getUsername())
                && userRepository.findAll().iterator().next().getPassword().equals(user.getPassword())) {

            // if (user.getUsername().equals(account) && user.getPassword().equals(pwd)) {
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

    @PostMapping("/test")
    public String signin(@RequestBody Map<String, String> map) {

        System.out.println(map);
        return map.get("username") + map.get("password");
    }

    @GetMapping("/topic")
    public Topic getdata(@RequestParam(value = "topic", defaultValue = "fhh", required = true) String topic) {

        System.out.println(topic);

        Topic t = new Topic();
        t.setContent(String.format("fhh do i with %s", topic));
        return t;
    }

}
