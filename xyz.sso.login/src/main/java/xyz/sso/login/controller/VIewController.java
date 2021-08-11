package xyz.sso.login.controller;

import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 处理页面跳转
 */
@RequestMapping("/view")
public class VIewController {

    @RequestMapping("/toLogin")
    public String toLogin() {
        return "login";
    }
}
