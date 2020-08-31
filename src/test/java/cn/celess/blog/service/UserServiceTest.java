package cn.celess.blog.service;

import cn.celess.blog.BaseTest;
import cn.celess.blog.enmu.UserAccountStatusEnum;
import cn.celess.blog.entity.model.PageData;
import cn.celess.blog.entity.model.UserModel;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class UserServiceTest extends BaseTest {
    @Autowired
    UserService userService;

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
}