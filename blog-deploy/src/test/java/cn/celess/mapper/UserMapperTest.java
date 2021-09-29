package cn.celess.mapper;

import cn.celess.BaseTest;
import cn.celess.common.enmu.RoleEnum;
import cn.celess.common.enmu.UserAccountStatusEnum;
import cn.celess.common.entity.User;
import cn.celess.common.mapper.UserMapper;
import cn.celess.common.util.MD5Util;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

public class UserMapperTest extends BaseTest {

    @Autowired
    UserMapper userMapper;


    @Test
    public void addUser() {
        User user = generateUser();
        assertNotNull(user.getId());
    }

    @Test
    public void updateInfo() {
        User user = generateUser();
        assertEquals(1, userMapper.updateInfo("ttt", "ttt", user.getId()));
    }

    @Test
    public void updateAvatarImgUrl() {
        User user = generateUser();
        assertEquals(1, userMapper.updateAvatarImgUrl("https://www.celess.cn/example.jpg", user.getId()));
    }

    @Test
    public void updateLoginTime() {
        User user = generateUser();
        assertEquals(1, userMapper.updateLoginTime(user.getEmail()));
    }

    @Test
    public void updateEmailStatus() {
        User user = generateUser();
        assertEquals(1, userMapper.updateEmailStatus(user.getEmail(), true));
    }

    @Test
    public void updatePwd() {
        User user = generateUser();
        assertEquals(1, userMapper.updatePwd(user.getEmail(), MD5Util.getMD5("12345687654")));
    }

    @Test
    public void getPwd() {
        User user = generateUser();
        assertEquals(user.getPwd(), userMapper.getPwd(user.getEmail()));
    }

    @Test
    public void existsByEmail() {
        User user = generateUser();
        assertTrue(userMapper.existsByEmail(user.getEmail()));
    }

    @Test
    public void findByEmail() {
        User user = generateUser();
        User byEmail = userMapper.findByEmail(user.getEmail());
        assertNotNull(byEmail);
        assertEquals(user.getId(), byEmail.getId());
    }

    @Test
    public void findById() {
        User user = generateUser();
        User findById = userMapper.findById(user.getId());
        assertNotNull(findById);
        assertEquals(user.getEmail(), findById.getEmail());
    }

    @Test
    public void getAvatarImgUrlById() {
        User user = generateUser();
        assertNull(userMapper.getAvatarImgUrlById(user.getId()));
        userMapper.updateAvatarImgUrl("example.cn", user.getId());
        assertEquals("example.cn", userMapper.getAvatarImgUrlById(user.getId()));

    }

    @Test
    public void getEmail() {
        User user = generateUser();
        assertEquals(user.getEmail(), userMapper.getEmail(user.getId()));
    }

    @Test
    public void getDisPlayName() {
        User user = generateUser();
        assertNull(userMapper.getDisPlayName(user.getId()));
    }

    @Test
    public void getRoleByEmail() {
        User user = generateUser();
        assertEquals(RoleEnum.USER_ROLE.getRoleName(), userMapper.getRoleByEmail(user.getEmail()));
    }

    @Test
    public void getRoleById() {
        User user = generateUser();
        assertEquals(RoleEnum.USER_ROLE.getRoleName(), userMapper.getRoleById(user.getId()));
    }

    @Test
    public void count() {
        generateUser();
        assertTrue(userMapper.count() >= 1);
    }

    @Test
    public void delete() {
        User user = generateUser();
        int delete = userMapper.delete(user.getId());
        assertEquals(1, delete);
        User byId = userMapper.findById(user.getId());
        assertEquals(UserAccountStatusEnum.DELETED.getCode(), byId.getStatus());
    }

    @Test
    public void lock() {
        User user = generateUser();
        int delete = userMapper.lock(user.getId());
        assertEquals(1, delete);
        User byId = userMapper.findById(user.getId());
        assertEquals(UserAccountStatusEnum.LOCKED.getCode(), byId.getStatus());
    }

    @Test
    public void setUserRole() {
        User user = generateUser();
        userMapper.setUserRole(user.getId(), RoleEnum.ADMIN_ROLE.getRoleName());

        assertEquals(RoleEnum.ADMIN_ROLE.getRoleName(), userMapper.getRoleById(user.getId()));
    }

    @Test
    public void findAll() {
        User user = generateUser();
        List<User> all = userMapper.findAll();
        assertTrue(all.size() >= 1);
    }

    @Test
    public void update() {
        User user = generateUser();
        user.setDesc("aaa");
        user.setDisplayName("bbb");
        user.setEmailStatus(true);
        user.setRole(RoleEnum.ADMIN_ROLE.getRoleName());
        user.setAvatarImgUrl("https://celess.cn/examcple.jpg");
        user.setEmail(randomStr(8) + "@celess.cn");
        user.setPwd(MD5Util.getMD5("010100000100000"));
        assertEquals(1, userMapper.update(user));
        User byId = userMapper.findById(user.getId());
        assertEquals(user.getDesc(), byId.getDesc());
        assertEquals(user.getDisplayName(), byId.getDisplayName());
        assertEquals(user.getEmailStatus(), byId.getEmailStatus());
        assertEquals(user.getRole(), byId.getRole());
        assertEquals(user.getAvatarImgUrl(), byId.getAvatarImgUrl());
        assertEquals(user.getEmail(), byId.getEmail());
        assertEquals(user.getPwd(), byId.getPwd());
    }

    private User generateUser() {
        User user = new User(randomStr(6) + "@celess.cn", MD5Util.getMD5("1234567890"));
        userMapper.addUser(user);
        User newUser = userMapper.findByEmail(user.getEmail());
        assertEquals(user.getId(), newUser.getId());
        return newUser;
    }
}