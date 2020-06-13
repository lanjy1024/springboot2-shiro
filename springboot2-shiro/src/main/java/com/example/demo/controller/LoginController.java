package com.example.demo.controller;



import com.example.demo.entity.User;
import com.example.demo.model.LoginResult;
import com.example.demo.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;


/**
 * @author：lanjy
 * @date：2020/6/11
 * @description：
 */
@Controller
@Slf4j
public class LoginController {
    @Autowired
    private LoginService loginService;

    @GetMapping(value = "/login")
    public String login() {
        return "login";
    }

    @PostMapping(value = "/login")
    public String login( User user) {
        log.info("LoginController======用户登录 ");
        /*String userName = user.getUserName();
        String password = user.getPassword();*/
        LoginResult loginResult = loginService.login(user);
        log.info("LoginController======用户登录 loginResult"+loginResult);
        if(loginResult.isLogin()){
            return "index";
        } else {
            return "403" ;
        }
    }

    @GetMapping("/logout")
    public String logout(RedirectAttributes attributes){
        loginService.logout();
        log.info("注销成功============");
        attributes.addFlashAttribute("message","您已退出，请重新登录！");
        return "redirect:/login";
    }

    @GetMapping(value = "/index")
    public String index() {
        log.info("========index============");
        return "index";
    }
    @GetMapping(value = "/test")
    public String test() {
        log.info("========test============");
        return "redirect:/index";
    }

    @GetMapping("/403")
    public String unauthorizedRole(){
        return "403";
    }
}