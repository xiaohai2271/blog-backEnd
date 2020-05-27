package cn.celess.blog.controller;

import cn.celess.blog.BaseTest;
import cn.celess.blog.entity.User;
import cn.celess.blog.entity.model.PageData;
import cn.celess.blog.entity.model.UserModel;
import cn.celess.blog.entity.request.LoginReq;
import cn.celess.blog.entity.request.UserReq;
import cn.celess.blog.mapper.UserMapper;
import cn.celess.blog.util.MD5Util;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static cn.celess.blog.enmu.ResponseEnum.*;

public class UserControllerTest extends BaseTest {

    @Autowired
    UserMapper userMapper;

    @Test
    public void login() throws Exception {
        assertNotNull(userLogin());
        assertNotNull(adminLogin());
        // 用户不存在
        LoginReq req = new LoginReq();
        req.setEmail("zh@celess.cn");
        req.setPassword("123456789");
        req.setIsRememberMe(false);
        JSONObject loginReq = JSONObject.fromObject(req);
        mockMvc.perform(post("/login").content(loginReq.toString()).contentType("application/json"))
                .andDo(result ->
                        assertEquals(USER_NOT_EXIST.getCode(), JSONObject.fromObject(result.getResponse().getContentAsString()).getInt(Code))
                );
    }

    @Test
    public void registration() {
        // ignore
    }

    @Test
    public void logout() throws Exception {
        mockMvc.perform(get("/logout")).andDo(result -> assertEquals(SUCCESS.getCode(), JSONObject.fromObject(result.getResponse().getContentAsString()).getInt(Code)));
        mockMvc.perform(get("/logout").header("Authorization", userLogin())).andDo(result -> assertEquals(SUCCESS.getCode(), JSONObject.fromObject(result.getResponse().getContentAsString()).getInt(Code)));
    }

    @Test
    public void updateInfo() throws Exception {
        String desc = UUID.randomUUID().toString().substring(0, 4);
        String disPlayName = UUID.randomUUID().toString().substring(0, 4);
        mockMvc.perform(put("/user/userInfo/update?desc=" + desc + "&displayName=" + disPlayName).header("Authorization", userLogin()))
                .andDo(result -> {
                    JSONObject object = JSONObject.fromObject(result.getResponse().getContentAsString());
                    assertEquals(SUCCESS.getCode(), object.getInt(Code));
                    UserModel u = (UserModel) JSONObject.toBean(object.getJSONObject(Result), UserModel.class);
                    assertEquals(desc, u.getDesc());
                    assertEquals(disPlayName, u.getDisplayName());
                    assertNotNull(u.getId());
                });
    }

    @Test
    public void getUserInfo() throws Exception {
        mockMvc.perform(get("/user/userInfo").header("Authorization", userLogin()))
                .andDo(result -> {
                    JSONObject object = JSONObject.fromObject(result.getResponse().getContentAsString());
                    assertEquals(SUCCESS.getCode(), object.getInt(Code));
                    UserModel u = (UserModel) JSONObject.toBean(object.getJSONObject(Result), UserModel.class);
                    assertNotNull(u.getId());
                    assertNotNull(u.getEmail());
                    assertNotNull(u.getDisplayName());
                    assertNotNull(u.getEmailStatus());
                    assertNotNull(u.getAvatarImgUrl());
                    assertNotNull(u.getDesc());
                    assertNotNull(u.getRecentlyLandedDate());
                    assertNotNull(u.getRole());
                });
    }

    @Test
    public void upload() throws Exception {
        URL url = new URL("https://56462271.oss-cn-beijing.aliyuncs.com/web/logo.png");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        InputStream inputStream = connection.getInputStream();
        assertNotNull(inputStream);
        MockMultipartFile file = new MockMultipartFile("file", "logo.png", MediaType.IMAGE_PNG_VALUE, inputStream);
        mockMvc.perform(multipart("/user/imgUpload").file(file)).andDo(result -> assertEquals(HAVE_NOT_LOG_IN.getCode(), JSONObject.fromObject(result.getResponse().getContentAsString()).getInt(Code)));
        mockMvc.perform(multipart("/user/imgUpload").file(file).header("Authorization", userLogin())).andDo(result -> {
            JSONObject object = JSONObject.fromObject(result.getResponse().getContentAsString());
            assertEquals(SUCCESS.getCode(), object.getInt(Code));
            assertNotNull(object.getString(Result));
        });
    }

    @Test
    public void sendResetPwdEmail() {
        // ignore
    }

    @Test
    public void sendVerifyEmail() {
        // ignore
    }

    @Test
    public void emailVerify() {
        // ignore
    }

    @Test
    public void resetPwd() {
        // ignore
    }

    @Test
    public void multipleDelete() throws Exception {
        List<User> userList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            String s = UUID.randomUUID().toString();
            String email = s.substring(s.length() - 4) + "@celess.cn";
            String pwd = MD5Util.getMD5("123456789");
            User user = new User(email, pwd);
            int i1 = userMapper.addUser(user);
            if (i1 == 0) {
                continue;
            }
            userList.add(userMapper.findByEmail(email));
            if (i == 9) {
                //设置一个管理员
                userMapper.setUserRole(userMapper.findByEmail(email).getId(), "admin");
            }
        }
        List<Long> idList = new ArrayList<>();
        userList.forEach(user -> idList.add(user.getId()));
        System.out.println("id :: == > " + idList.toString());
        mockMvc.perform(delete("/admin/user/delete").content(idList.toString()).contentType("application/json"))
                .andDo(result -> assertEquals(HAVE_NOT_LOG_IN.getCode(), JSONObject.fromObject(result.getResponse().getContentAsString()).getInt(Code)));
        mockMvc.perform(delete("/admin/user/delete").content(idList.toString()).contentType("application/json").header("Authorization", userLogin()))
                .andDo(result -> assertEquals(PERMISSION_ERROR.getCode(), JSONObject.fromObject(result.getResponse().getContentAsString()).getInt(Code)));
        mockMvc.perform(delete("/admin/user/delete").content(idList.toString()).contentType("application/json").header("Authorization", adminLogin()))
                .andDo(result -> {
                    JSONObject object = JSONObject.fromObject(result.getResponse().getContentAsString());
                    assertEquals(SUCCESS.getCode(), object.getInt(Code));
                    JSONArray jsonArray = object.getJSONArray(Result);
                    jsonArray.forEach(o -> {
                        JSONObject json = JSONObject.fromObject(o);
                        // 判断响应数据中是否包含输入的id
                        assertTrue(idList.contains((long) json.getInt("id")));
                        // 判断处理状态
                        boolean status = json.getBoolean("status");
                        if (json.containsKey("msg"))
                            assertFalse(status);
                        else
                            assertTrue(status);
                    });
                });

    }

    @Test
    public void updateInfoByAdmin() throws Exception {
        UserReq userReq = new UserReq();
        String email = UUID.randomUUID().toString().substring(0, 4) + "@celess.cn";
        User user = new User(email, MD5Util.getMD5("123456789"));
        userMapper.addUser(user);
        User userByDb = userMapper.findByEmail(email);
        userReq.setId(userByDb.getId());
        userReq.setPwd(UUID.randomUUID().toString().replaceAll("-", "").substring(0, 10));
        userReq.setDesc(UUID.randomUUID().toString());
        userReq.setEmailStatus(new Random().nextBoolean());
        userReq.setRole("admin");
        userReq.setDisplayName(UUID.randomUUID().toString().substring(0, 4));
        userReq.setEmail(UUID.randomUUID().toString().substring(0, 5) + "@celess.cn");
        mockMvc.perform(put("/admin/user").contentType("application/json").content(JSONObject.fromObject(userReq).toString()))
                .andDo(result -> assertEquals(HAVE_NOT_LOG_IN.getCode(), JSONObject.fromObject(result.getResponse().getContentAsString()).getInt(Code)));
        mockMvc.perform(put("/admin/user").contentType("application/json").header("Authorization", userLogin()).content(JSONObject.fromObject(userReq).toString()))
                .andDo(result -> assertEquals(PERMISSION_ERROR.getCode(), JSONObject.fromObject(result.getResponse().getContentAsString()).getInt(Code)));
        mockMvc.perform(put("/admin/user").contentType("application/json").header("Authorization", adminLogin()).content(JSONObject.fromObject(userReq).toString()))
                .andDo(result -> {
                    JSONObject object = JSONObject.fromObject(result.getResponse().getContentAsString());
                    assertEquals(SUCCESS.getCode(), object.getInt(Code));
                    UserModel userModel = (UserModel) JSONObject.toBean(object.getJSONObject(Result), UserModel.class);
                    assertEquals(userReq.getId(), userModel.getId());
                    assertEquals(userReq.getRole(), userModel.getRole());
                    assertEquals(userReq.getEmail(), userModel.getEmail());
                    assertEquals(userReq.getDesc(), userModel.getDesc());
                    assertEquals(userReq.getDisplayName(), userModel.getDisplayName());
                });
    }

    @Test
    public void getAllUser() throws Exception {
        mockMvc.perform(get("/admin/users?page=1&count=10"))
                .andDo(result -> assertEquals(HAVE_NOT_LOG_IN.getCode(), JSONObject.fromObject(result.getResponse().getContentAsString()).getInt(Code)));
        mockMvc.perform(get("/admin/users?page=1&count=10").header("authorization", userLogin()))
                .andDo(result -> assertEquals(PERMISSION_ERROR.getCode(), JSONObject.fromObject(result.getResponse().getContentAsString()).getInt(Code)));
        mockMvc.perform(get("/admin/users?page=1&count=10").header("Authorization", adminLogin()))
                .andDo(result -> {
                    JSONObject object = JSONObject.fromObject(result.getResponse().getContentAsString());
                    assertEquals(SUCCESS.getCode(), object.getInt(Code));
                    // 结果集非空
                    assertNotNull(object.getJSONObject(Result));
                    // 判断pageInfo是否包装完全
                    JSONObject resultJson = JSONObject.fromObject(object.getJSONObject(Result));
                    PageData<UserModel> pageData = (PageData<UserModel>) JSONObject.toBean(resultJson, PageData.class);
                    assertNotEquals(0, pageData.getTotal());
                    assertEquals(1, pageData.getPageNum());
                    assertEquals(10, pageData.getPageSize());
                    // 内容完整
                    for (Object user : pageData.getList()) {
                        UserModel u = (UserModel) JSONObject.toBean(JSONObject.fromObject(user), UserModel.class);
                        assertNotNull(u.getId());
                        assertNotNull(u.getEmail());
                        assertNotNull(u.getRole());
                        assertNotNull(u.getEmailStatus());
                        assertNotNull(u.getDisplayName());
                    }
                });
    }

    @Test
    public void getEmailStatus() throws Exception {
        String email = UUID.randomUUID().toString().substring(0, 4) + "@celess.cn";
        mockMvc.perform(get("/emailStatus/" + email)).andDo(result -> {
            String content = result.getResponse().getContentAsString();
            assertEquals(SUCCESS.getCode(), JSONObject.fromObject(content).getInt(Code));
            assertFalse(JSONObject.fromObject(content).getBoolean(Result));
        });
        email = "a@celess.cn";
        mockMvc.perform(get("/emailStatus/" + email)).andDo(result -> {
            String content = result.getResponse().getContentAsString();
            assertEquals(SUCCESS.getCode(), JSONObject.fromObject(content).getInt(Code));
            assertTrue(JSONObject.fromObject(content).getBoolean(Result));
        });
    }

    @Test
    public void setPwd() throws Exception {
        String email = UUID.randomUUID().toString().substring(0, 4) + "@celess.cn";
        assertEquals(1, userMapper.addUser(new User(email, MD5Util.getMD5("1234567890"))));
        LoginReq req = new LoginReq();
        req.setEmail(email);
        req.setPassword("1234567890");
        req.setIsRememberMe(false);
        JSONObject loginReq = JSONObject.fromObject(req);
        String contentAsString = mockMvc.perform(post("/login").content(loginReq.toString()).contentType("application/json")).andReturn().getResponse().getContentAsString();
        assertNotNull(contentAsString);
        String token = JSONObject.fromObject(contentAsString).getJSONObject(Result).getString("token");
        assertNotNull(token);
        MultiValueMap<String, String> param = new LinkedMultiValueMap<String, String>();
        param.add("pwd", "1234567890");
        param.add("newPwd", "aaabbbccc");
        param.add("confirmPwd", "aaabbbccc");
        mockMvc.perform(post("/user/setPwd").header("Authorization", token).params(param)).andDo(result -> {
            String content = result.getResponse().getContentAsString();
            assertEquals(SUCCESS.getCode(), JSONObject.fromObject(content).getInt(Code));
            assertEquals(MD5Util.getMD5("aaabbbccc"), userMapper.getPwd(email));
        });
    }
}