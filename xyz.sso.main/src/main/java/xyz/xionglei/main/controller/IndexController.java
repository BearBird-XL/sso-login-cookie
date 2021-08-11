package xyz.xionglei.main.controller;

import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/index")
public class IndexController {

    @RequestMapping("/index")
    public String toIndex() {
        return "index";
    }
}
