package xyz.sso.login.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import xyz.sso.login.pojo.BspUser;
import xyz.sso.login.pojo.User;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Controller
@RequestMapping("/login")
public class LoginController {

    @RequestMapping("/toLogin")
    public String toLogin(Model model, @ModelAttribute(name = "redirect") String redirect) {
        System.out.println("LoginController.toLogin");
        System.out.println("model = " + model + ", redirect = " + redirect);
        model.addAttribute("red_url", redirect);
        return "login";
    }

    @RequestMapping("/doLogin")
    public String doLogin(HttpServletRequest request,
                          RedirectAttributes redirectAttributes,
                          @RequestParam(defaultValue = "https://qq.com") String redirect,
                          String username,
                          String password) {
        System.out.println("LoginController.doLogin");
        System.out.println("request = " + request + ", redirectAttributes = " + redirectAttributes + ", redirect = " + redirect + ", username = " + username + ", password = " + password);
        UUID uuid = UUID.randomUUID();
        User user = new User(uuid.toString(), username, password);
        BspUser bspUser = new BspUser(uuid.toString(), user);
        request.getSession().setAttribute("user", bspUser);
        redirectAttributes.addFlashAttribute("code", bspUser.getId());
        redirectAttributes.addFlashAttribute("redirect", redirect);
        return "redirect:/sso/success";
    }
}
