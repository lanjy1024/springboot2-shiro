package com.example.demo.dao;

import com.example.demo.entity.User;

/**
 * @author：lanjy
 * @date：2020/6/11
 * @description：
 */
public interface UserRepository extends BaseRepository<User,Long> {
    User findByUserName(String userName);
}
