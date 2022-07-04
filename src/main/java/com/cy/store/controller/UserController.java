package com.cy.store.controller;

import com.cy.store.entity.User;
import com.cy.store.service.UserService;
import com.cy.store.service.ex.InsertException;
import com.cy.store.service.ex.UsernameException;
import com.cy.store.util.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("users")
public class UserController extends BaseController{
    @Autowired(required = false)
    private UserService userService;

    @RequestMapping("reg")

    public JsonResult<Void> reg(User user){


        userService.reg(user);

        return new JsonResult<>(OK);
    }
//    @RequestMapping("login")
//    public JsonResult<User> login(String username,String password){
//        User data=userService.login(username,password);
//        return new JsonResult<User>(OK,data);
//    }
    @RequestMapping("login")
    public JsonResult<User> login(String username, String password, HttpSession session) {
        // 调用业务对象的方法执行登录，并获取返回值
        User data = userService.login(username, password);
        //登录成功后，将uid和username存入到HttpSession中
        session.setAttribute("uid", data.getUid());
        session.setAttribute("username", data.getUsername());
         System.out.println("Session中的uid=" + getuidFromSession(session));
         System.out.println("Session中的username=" + getUsernameFromSession(session));
        // 将以上返回值和状态码OK封装到响应结果中并返回
        return new JsonResult<User>(OK, data);
    }

    /**
     * 修改密码
     * @param oldPassword
     * @param newPassword
     * @param session
     * @return
     */
    @RequestMapping("chang_password")
    public JsonResult<Void> changPassword(String oldPassword,String newPassword,HttpSession session){
        Integer uid=getuidFromSession(session);
        String username=getUsernameFromSession(session);
        userService.changPassword(uid,username,oldPassword,newPassword);
        return new JsonResult<>(OK);
    }

    /**
     * 点到个人资料页面将用户信息显示到页面上面
     * @param session
     * @return
     */
    @RequestMapping("get_by_uid")
    public JsonResult<User> geetByUid(HttpSession session){
        User data=userService.getByUid(getuidFromSession(session));
        return new JsonResult<>(OK,data);
    }

    /**
     * 个人资料修改
     * @param user
     * @param session
     * @return
     */
    @RequestMapping("change_info")
    public JsonResult<Void> changeInfo(User user, HttpSession session) {
        // 从HttpSession对象中获取uid和username
        Integer uid = getuidFromSession(session);
        String username = getUsernameFromSession(session);
        // 调用业务对象执行修改用户资料
        userService.changeInfo(uid, username, user);
        // 响应成功
         return new JsonResult<>(OK); }
 }
