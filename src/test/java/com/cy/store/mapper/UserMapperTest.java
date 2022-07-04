package com.cy.store.mapper;

import com.cy.store.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

//表明这个是测试类，不会随同项目启动
@SpringBootTest
@RunWith(SpringRunner.class)
public class UserMapperTest {
    /**
     * 1.必须被@Test修饰
     * 2.返回值必须是void
     * 3.方法的参数列表不指定任何类型
     * 4.方法的访问修饰必须是public
     */
    //动态代理技术来解决
    @Autowired(required=false)
    private UserMapper userMapper;
    @Test
    public void insert(){
        User user = new User();
        user.setUsername("zhang");
        user.setPassword("123");
        Integer rows = userMapper.insert(user);
        System.out.println(rows);
    }
    @Test
    public void findByUsername(){
        User zhang = userMapper.findByUserName("zhang");
        System.out.println(zhang);
    }
}
