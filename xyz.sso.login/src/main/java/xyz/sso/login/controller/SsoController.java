package xyz.sso.login.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import xyz.sso.login.pojo.BspUser;
import xyz.sso.login.pojo.User;

import javax.servlet.http.HttpServletRequest;

/**
 * @author xionglei
 * @create 2021-08-30 15:06
 */
@Controller
@RequestMapping("/sso")
public class SsoController {

    @RequestMapping("/oauth2.0/authorize")
    public String oauth(@RequestParam(name = "client_id") String clientId,
                        @RequestParam(name = "response_type") String responseType,
                        @RequestParam(name = "redirect_uri") String redirectUrl,
                        @RequestParam(name = "oauth_timestamp") String oauthTimestamp,
                        @RequestParam(required = false, name = "state") String state,
                        HttpServletRequest request,
                        RedirectAttributes attributes) {
        System.out.println("\n--------------------------");
        System.out.println("SsoController.oauth----------");
        System.out.println("session: " + request.getSession());
        System.out.println("sessionId: " + request.getSession().getId());
        Object user = request.getSession().getAttribute("user");
        attributes.addFlashAttribute("redirect", redirectUrl);
//         测试环境直接返回code
//        if (user == null) {
//            return "redirect:/login/toLogin";
//        } else {
//
//        }
        attributes.addFlashAttribute("code", "code-code-code");
        return "redirect:/sso/success";

    }

    @ResponseBody
    @RequestMapping(value = "/getUser", method = RequestMethod.GET)
    public BspUser getUser(HttpServletRequest request, @RequestParam(name = "access_token") String token) {
        System.out.println("\n--------------------------");
        System.out.println("SsoController.getUser");
        System.out.println("session: " + request.getSession());
        System.out.println("sessionId: " + request.getSession().getId());
        // 测试：返回User信息写死
        // 注： username 对应的是应用系统的用户名
       return new BspUser("Maple", new User("123", "cs", "ui"));
    }

    @ResponseBody
    @RequestMapping(value = "/getToken", method = RequestMethod.POST)
    public String getToken(HttpServletRequest request, String code) {
        System.out.println("\n--------------------------");
        System.out.println("SsoController.getToken");
        System.out.println("session: " + request.getSession());
        System.out.println("sessionId: " + request.getSession().getId());
        BspUser user;
        return (user = ((BspUser) request.getSession().getAttribute("user"))) == null ? "testToken" : user.getId();

    }

    @RequestMapping("/success")
    public String success(RedirectAttributes attributes,
                          @ModelAttribute("code") String code,
                          @ModelAttribute("redirect") String redirect) {
        System.out.println("SsoController.success");
        System.out.println("attributes = " + attributes + ", code = " + code + ", redirect = " + redirect);
        return "redirect:" + redirect + "?code=" + code + "&state=";
    }
}
