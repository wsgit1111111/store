package com.cy.store.service;

import com.cy.store.entity.User;

/**
 * 用户模块业务层
 */
public interface UserService {
    /**
     * 用户注册
     * @param user
     */
    void reg(User user);

    /**
     * 用户登录功能
     * @param username
     * @param password
     * @reture 当前匹配的用户数据，如果没有则返回null
     */
    User login(String username,String password);

    void changPassword(Integer uid,String username,String oldPassword,String newPassword);

    /*** 获取当前登录的用户的信息
     * * @param uid 当前登录的用户的id
     * * @return 当前登录的用户的信息
     * */
    User getByUid(Integer uid);
    /*** 修改用户资料
     * * @param uid 当前登录的用户的id
     * * @param username 当前登录的用户名
     * * @param user 用户的新的数据 */
    void changeInfo(Integer uid, String username, User user);
}