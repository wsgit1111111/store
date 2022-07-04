package com.cy.store.mapper;

import com.cy.store.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;

/**
 * 用户模块的持久层操作
 */
//@Mapper
public interface UserMapper {
    /**
     * 插入用户的数据
     * 用户的数据，返回受影响的行数（增，删，改，查都有受影响的行数作为返回值，可以根据返回值来判断是否执行成功）
     */
    Integer insert(User user);

    /**
     * 根据用户名来查询用户
     * @param username 用户名
     * @return  如果找到对应的用户
     */
    User findByUserName(String username);

    /**
     *  根据用户的uid来更改用户的密码
     *      * @param uid 用户的id
     *      * @return 返回影响的行数
     * @param uid
     * @param password 用户输入的新密码
     * @param modifiedUser 表示修改者
     * @param modifiedTime 表示修改时间
     * @return
     */
    Integer updataPasswordByUid(Integer uid, String password, String modifiedUser, Date modifiedTime);

    /**
     * 根据用户的id来查询用户的数据
     * @param uid 用户的id
     * @return 如果找到返回对象，否则返回null
     */
    User findByUid(Integer uid);

    /***
     * 根据uid更新用户资料
     * * @param user
     * 封装了用户id和新个人资料的对象
     * * @return 受影响的行数 */
    Integer updateInfoByUid(User user);
}
