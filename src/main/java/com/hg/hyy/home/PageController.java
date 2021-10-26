package com.hg.hyy.home;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

import com.hg.hyy.vue.User;

/**
 * Created by hyy on 2021/09/27.
 */
@Controller
@RequestMapping("/")
public class PageController {

    @GetMapping("/")
    public String index() {
        return "index";
    }


    @GetMapping("/test")
    public String test() {
        // int i = 1 / 0;//服务器内部运行异常 跳转500页面
        return "500";
    }

    @GetMapping("/admin")
    public String admin(Model model) {
        List<User> userList = new ArrayList<>();
        User user = new User("admin", "admin", "管理员");
        User user1 = new User("user", "123456", "访客");
        userList.add(user);
        userList.add(user1);
        model.addAttribute("users", userList);
        model.addAttribute("user", user);
        model.addAttribute("user1", user1);
        return "user";
    }
}