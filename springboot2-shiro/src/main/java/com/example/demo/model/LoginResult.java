package com.example.demo.model;

import lombok.Data;

/**
 * @author：lanjy
 * @date：2020/6/11
 * @description：
 */
@Data
public class LoginResult {
    private boolean isLogin = false;
    private String result;
}
