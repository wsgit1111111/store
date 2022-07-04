package com.cy.store.service.impl;

import com.cy.store.entity.User;
import com.cy.store.mapper.UserMapper;
import com.cy.store.service.ex.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.UUID;
@Service
public class UserServiceImpl implements com.cy.store.service.UserService {
    @Autowired(required=false)
    private UserMapper userMapper;

    @Override
    public void reg(User user) {
        //获取前端传来的用户名
        String username = user.getUsername();
        //查找用户名看看是否注册
        User result = userMapper.findByUserName(username);
        //判断结果集是否为空，知否注册过
        if (result != null) {
            //说明被注册过了，直接抛出异常
            throw new UsernameException("用户名被占用");
        }
        //密码进行加密String+password+String使用md5进行加密
        String oldpassword = user.getPassword();
        String salt = UUID.randomUUID().toString().toUpperCase();
        //补全数据:盐值的记录
        user.setSalt(salt);
        //将密码和盐值作为一个整体进行加密处理
        String md5Password = getMD5Password(oldpassword, salt);

        //将加密以后的数据重新存入数据库中
        user.setPassword(md5Password);
        //补全数据:is_delete
        user.setIsDelete(0);

        //补全数据:4个日志信息
        user.setCreatedUser(user.getUsername());
        user.setModifiedUser(user.getUsername());
        Date date = new Date();
        user.setCreatedTime(date);
        user.setModifiedTime(date);
        //执行注册业务
        Integer rows = userMapper.insert(user);
        if (rows != 1) {
            throw new InsertException("在用户注册过程中产生了未知的异常");
        }
    }

    @Override
    public User login(String username, String password) {
        //先根据用户名称查询用户数据是否存在，如果不存在就抛异常
        User result = userMapper.findByUserName(username);//返回的是一个user对象
        if (result==null){
            throw new UserNotFoundException("用户数据不存在");
        }
        //检测用户密码是否匹配
        //1.获取数据库中的加密密码
        String matchPassword=result.getPassword();
        //2.和用户的传递过来的密码进行比较
        //2.1先获取盐值：上一次注册自动生成的盐值，盐值在User属性中，result中可以直接获取
        String salt=result.getSalt();
        //2.2将用户的密码按照相同的md5算法规则进行加密
        String newMD5Password=getMD5Password(password,salt);
        //3.将密码进行比较
        if (!newMD5Password.equals(matchPassword)){
            throw new PasswordNotMatchException("用户密码错误");
        }
        //加一个判断用户是否被删除 is_delete 1代表被删除
        if (result.getIsDelete()==1){
            throw new UserNotFoundException("用户已删除,用户数据不存在");
        }
//        User user= userMapper.findByUserName(username);
        //将当前的用户数据进行返回，为了辅助其他页面,调用数据量越小越好提升数据的性能
        User user = new User();
        user.setUid(result.getUid());
        user.setUsername(result.getUsername());
        user.setAvatar(result.getAvatar());
        return user;
    }

    @Override
    public void changPassword(Integer uid, String username, String oldPassword, String newPassword) {
        // 调用userMapper的findByUid()方法，根据参数uid查询用户数据
        User result = userMapper.findByUid(uid);
        // 检查查询结果是否为null
        if (result == null) { // 是：抛出UserNotFoundException异常
            throw new UserNotFoundException("用户数据不存在");
        }
        // 检查查询结果中的isDelete是否为1
        if (result.getIsDelete().equals(1)) { // 是：抛出UserNotFoundException异常
             throw new UserNotFoundException("用户数据不存在");
        }
        // 从查询结果中取出盐值
        String salt = result.getSalt();
        // 将参数oldPassword结合盐值加密，得到oldMd5Password
        String oldMd5Password = getMD5Password(oldPassword, salt);
        // 判断查询结果中的password与oldMd5Password是否不一致
        if (!result.getPassword().contentEquals(oldMd5Password)) { // 是：抛出PasswordNotMatchException异常
             throw new PasswordNotMatchException("原密码错误"); }
        // 将参数newPassword结合盐值加密，得到newMd5Password
        String newMd5Password = getMD5Password(newPassword, salt);
        // 创建当前时间对象
        Date now = new Date();
        // 调用userMapper的updatePasswordByUid()更新密码，并获取返回值
        Integer rows = userMapper.updataPasswordByUid(uid, newMd5Password, username, now);
        // 判断以上返回的受影响行数是否不为1
        if (rows != 1) { // 是：抛出UpdateException异常
             throw new UpdateException("更新用户数据时出现未知错误，请联系系统管理员"); }
    }

    @Override
    public User getByUid(Integer uid) {
        // 调用userMapper的findByUid()方法，根据参数uid查询用户数据
        User result = userMapper.findByUid(uid);
        // 判断查询结果是否为null
        if (result == null) {
            // 是：抛出UserNotFoundException异常
             throw new UserNotFoundException("用户数据不存在");
        }
        // 判断查询结果中的isDelete是否为1
        if (result.getIsDelete().equals(1)) {
            // 是：抛出UserNotFoundException异常
            throw new UserNotFoundException("用户数据不存在"); }
        // 创建新的User对象
        User user = new User();
        // 将以上查询结果中的username/phone/email/gender封装到新User对象中
        user.setUsername(result.getUsername());
        user.setPhone(result.getPhone());
        user.setEmail(result.getEmail());
        user.setGender(result.getGender());
        // 返回新的User对象
        return user;
    }

    @Override
    public void changeInfo(Integer uid, String username, User user) {
        // 调用userMapper的findByUid()方法，根据参数uid查询用户数据
        User result = userMapper.findByUid(uid);
        // 判断查询结果是否为null
        if (result == null) {
        // 是：抛出UserNotFoundException异常
            throw new UserNotFoundException("用户数据不存在"); }
        // 判断查询结果中的isDelete是否为1
        if (result.getIsDelete().equals(1)) {
            // 是：抛出UserNotFoundException异常
            throw new UserNotFoundException("用户数据不存在"); }
        // 向参数user中补全数据：uid
        user.setUid(uid);
        // 向参数user中补全数据：modifiedUser(username)
        user.setModifiedUser(username);
        // 向参数user中补全数据：modifiedTime(new Date())
        user.setModifiedTime(new Date());
        // 调用userMapper的updateInfoByUid(User user)方法执行修改，并获取返回值
        Integer rows = userMapper.updateInfoByUid(user);
        // 判断以上返回的受影响行数是否不为1
        if (rows != 1) {
            // 是：抛出UpdateException异常
            throw new UpdateException("更新用户数据时出现未知错误，请联系系统管理员"); }
    }


    /**
     * 定义一个md5
     */
    private String getMD5Password(String password,String salt){
        for (int i=0;i<3;i++){
            password=DigestUtils.md5DigestAsHex((salt+password+salt).getBytes()).toUpperCase();
        }
        return password;
    }
}
