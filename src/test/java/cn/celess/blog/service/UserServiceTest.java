package cn.celess.blog.service;

import cn.celess.blog.BaseTest;
import cn.celess.blog.enmu.ResponseEnum;
import cn.celess.blog.enmu.UserAccountStatusEnum;
import cn.celess.blog.entity.User;
import cn.celess.blog.entity.model.PageData;
import cn.celess.blog.entity.model.UserModel;
import cn.celess.blog.entity.request.LoginReq;
import cn.celess.blog.exception.MyException;
import cn.celess.blog.mapper.UserMapper;
import cn.celess.blog.util.MD5Util;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class UserServiceTest extends BaseTest {
    @Autowired
    UserService userService;
    @Autowired
    UserMapper userMapper;

    @Test
    public void getUserList() {
        //  测试status 参数
        PageData<UserModel> userList = userService.getUserList(1, 10, UserAccountStatusEnum.NORMAL.getCode());
        assertTrue(userList.getList().stream().allMatch(userModel -> userModel.getStatus().getCode() == UserAccountStatusEnum.NORMAL.getCode()));
        userList = userService.getUserList(1, 10, UserAccountStatusEnum.LOCKED.getCode());
        assertTrue(userList.getList().stream().allMatch(userModel -> userModel.getStatus().getCode() == UserAccountStatusEnum.LOCKED.getCode()));
        userList = userService.getUserList(1, 10, UserAccountStatusEnum.DELETED.getCode());
        assertTrue(userList.getList().stream().allMatch(userModel -> userModel.getStatus().getCode() == UserAccountStatusEnum.DELETED.getCode()));
        userList = userService.getUserList(1, 10, null);
        assertTrue(userList.getList().stream().anyMatch(userModel -> userModel.getStatus().getCode() == UserAccountStatusEnum.NORMAL.getCode()));
        assertTrue(userList.getList().stream().anyMatch(userModel -> userModel.getStatus().getCode() == UserAccountStatusEnum.LOCKED.getCode()));
        assertTrue(userList.getList().stream().anyMatch(userModel -> userModel.getStatus().getCode() == UserAccountStatusEnum.DELETED.getCode()));
    }

    @Test
    public void testLogin() {
        // 测试账户 被锁 被删除  登录
        String email = randomStr(5) + "@celess.cn";
        String pwd = MD5Util.getMD5("123456789");
        User user = new User(email, pwd);
        userMapper.addUser(user);
        assertNotNull(user.getId());

        user = userMapper.findByEmail(email);
        LoginReq loginReq = new LoginReq(email, "123456789", false);
        UserModel login = userService.login(loginReq);
        assertEquals(UserAccountStatusEnum.NORMAL, login.getStatus());

        userMapper.lock(user.getId());
        try {
            userService.login(loginReq);
            fail("测试登录被锁账户 失败！");
        } catch (MyException e) {
            assertEquals(ResponseEnum.CAN_NOT_USE.getCode(), e.getCode());
            assertEquals(UserAccountStatusEnum.LOCKED, e.getResult());
        }

        userMapper.delete(user.getId());
        try {
            userService.login(loginReq);
            fail("测试登录被删除账户 失败！");
        } catch (MyException e) {
            assertEquals(ResponseEnum.CAN_NOT_USE.getCode(), e.getCode());
            assertEquals(UserAccountStatusEnum.DELETED, e.getResult());
        }


    }
}