package com.cy.store.service;

import com.cy.store.entity.User;
import com.cy.store.mapper.UserMapper;
import com.cy.store.service.ex.ServiceException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

//表明这个是测试类，不会随同项目启动
@SpringBootTest
@RunWith(SpringRunner.class)
public class UserServicesTest {
    /**
     * 1.必须被@Test修饰
     * 2.返回值必须是void
     * 3.方法的参数列表不指定任何类型
     * 4.方法的访问修饰必须是public
     */
    //动态代理技术来解决
    @Autowired(required=false)
    private UserService userService;
    @Autowired(required=false)
    private UserMapper userMapper;
    @Test
    public void reg(){
        try {
            User user = new User();
            user.setUsername("san2");
            user.setPassword("123");
            userService.reg(user);
            System.out.println("OK");
        } catch (ServiceException e) {
            //获取类的对象
            System.out.println(e.getClass().getSimpleName());
        }
    }
    @Test
    public void login(){
        User user= userService.login("WJT","123");
        System.out.println(user);
    }

    /**
     * 不经过业务层修改密码
     */
    @Test
    public void updataPasswordByUid(){
        userMapper.updataPasswordByUid(6,"123456","管理员",new Date());
    }

    /**
     * 通过id查找用户
     */
    @Test
    public void findByUid(){
        System.out.println(userMapper.findByUid(6));
    }

    /**
     * 修改密码
     */
    @Test public void changePassword() {
        try {Integer uid = 3;
            String username = "san2";
            String oldPassword = "321";
            String newPassword = "888888";
            userService.changPassword(uid, username, oldPassword, newPassword);
            System.out.println("密码修改成功！");
        } catch (ServiceException e) {
            System.out.println("密码修改失败！" + e.getClass().getSimpleName()); System.out.println(e.getMessage());
        }
    }

    @Test public void getByUid() {
        try {Integer uid =6;
            User user = userService.getByUid(uid);
            System.out.println(user);
        } catch (ServiceException e) {
            System.out.println(e.getClass().getSimpleName());
            System.out.println(e.getMessage()); } }
    @Test
    public void updateInfoByUid(){
        User user = new User();
        user.setUid(3);
        user.setPhone("11112222");
        user.setEmail("1234@qq.com");
        user.setGender(1);
        userMapper.updateInfoByUid(user);
    }
}
