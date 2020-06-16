package com.example.demo.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author：lanjy
 * @date：2020/6/16
 * @description：
 */
@Controller
@RequestMapping("/order")
public class OrderController {

    @GetMapping("/list")
    @RequiresPermissions("user:view")//权限管理;
    public String orderList(){
        return "order_list";
    }
}
