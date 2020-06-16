package com.example.demo.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author：lanjy
 * @date：2020/6/11
 * @description：UserController用来测试访问，权限全部采用注解的方式。
 */
@Controller
@RequestMapping("/user")
public class UserController {
    //用户查询
    @GetMapping("/userlists")
    @RequiresPermissions("user:view")//权限管理;
    public String userInfo(){
        return "user_list";
    }

    //用户添加
    @GetMapping("/userAdd")
    @ResponseBody
    @RequiresPermissions("user:add")//权限管理;
    public String userInfoAdd(){
        return "userAdd";
    }

    //用户删除
    @GetMapping("/userDel")
    @ResponseBody
    @RequiresPermissions("user:del")//权限管理;
    public String userDel(){
        return "userDel";
    }



}
