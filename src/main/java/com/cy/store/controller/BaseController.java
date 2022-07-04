package com.cy.store.controller;

import com.cy.store.service.ex.*;
import com.cy.store.util.JsonResult;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpSession;

/**
 * 控制层类的基类：异常的捕获处理
 */
public class BaseController {
    //正常的状态码,操作成功的状态码；
    public static final int OK=200;

    //请求处理的方法，这个方法的返回值就是需要传递前端的数据
    //当项目产生异常会被统一拦截到此方法中
    @ExceptionHandler(ServiceException.class) //统一抛出处理的异常
    public JsonResult<Void> handleException(Throwable e){
        JsonResult<Void> result=new JsonResult<>(e);
        if (e instanceof UsernameException){
            result.setState(4000);
            result.setMessage("用户名已经被占用");
        }else if (e instanceof InsertException){
            result.setState(5000);
            result.setMessage("注册是产生未知异常");
        }
        else if (e instanceof UserNotFoundException){
            result.setState(5001);
            result.setMessage("用户数据不存在的异常");
        }else if (e instanceof PasswordNotMatchException){
            result.setState(5002);
            result.setMessage("用户密码错误错误的异常");
        }
        else if (e instanceof UpdateException){
            result.setState(5001);
            result.setMessage("更新数据时产生异常");
        }
        return result;
    }

    /**
     * 获取session对象的uid
     * @param session
     * @return 当前登录用户的uid
     */
    protected final Integer getuidFromSession(HttpSession session){
        return Integer.valueOf(session.getAttribute("uid").toString());
    }

    /**
     * 获取当前用户的名字username
     * @param session
     * @return 当前登录用户的用户名
     */
    protected final String getUsernameFromSession(HttpSession session){
        return session.getAttribute("username").toString();
    }
}
