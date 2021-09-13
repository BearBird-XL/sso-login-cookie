package xyz.sso.login.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 处理页面跳转
 */
@Controller
@RequestMapping("/view")
public class VIewController {

    @RequestMapping("/toLogin")
    public String toLogin() {
        return "login";
    }

    @RequestMapping("/toSctj")
    public String toSctj() {
        return "sctj";
    }
}
