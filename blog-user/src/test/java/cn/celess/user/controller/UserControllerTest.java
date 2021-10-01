package cn.celess.user.controller;

import cn.celess.common.entity.Response;
import cn.celess.common.entity.User;
import cn.celess.common.entity.dto.LoginReq;
import cn.celess.common.entity.dto.UserReq;
import cn.celess.common.entity.vo.PageData;
import cn.celess.common.entity.vo.UserModel;
import cn.celess.common.mapper.UserMapper;
import cn.celess.common.service.UserService;
import cn.celess.common.util.RedisUtil;
import cn.celess.common.util.StringUtil;
import cn.celess.user.UserBaseTest;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static cn.celess.common.enmu.ResponseEnum.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


public class UserControllerTest extends UserBaseTest {

    @Autowired
    UserMapper userMapper;
    @Autowired

    RedisUtil redisUtil;
    private static final TypeReference<?> USER_MODEL_TYPE = new TypeReference<Response<UserModel>>() {
    };
    private static final TypeReference<?> USER_MODEL_PAGE_TYPE = new TypeReference<Response<PageData<UserModel>>>() {
    };
    private static final TypeReference<?> USER_MODEL_LIST_TYPE = new TypeReference<Response<List<Map<String, Object>>>>() {
    };
    @Autowired
    UserService userService;


    @Test
    public void login() throws Exception {
        assertNotNull(userLogin());
        assertNotNull(adminLogin());
        // 用户不存在
        LoginReq req = new LoginReq();
        req.setEmail("zh@celess.cn");
        req.setPassword("123456789");
        req.setIsRememberMe(false);
        getMockData(post("/login"), null, req).andDo(result -> assertEquals(USER_NOT_EXIST.getCode(), getResponse(result, STRING_TYPE).getCode()));
    }

    @Test
    public void registration() {
        // ignore
    }

    @Test
    public void logout() throws Exception {
        getMockData(get("/logout")).andDo(result -> assertEquals(SUCCESS.getCode(), getResponse(result, STRING_TYPE).getCode()));
        getMockData(get("/logout"), adminLogin()).andDo(result -> assertEquals(SUCCESS.getCode(), getResponse(result).getCode()));
    }

    @Test
    public void updateInfo() throws Exception {
        String desc = randomStr(4);
        String disPlayName = randomStr(4);
        getMockData(put("/user/userInfo/update?desc=" + desc + "&displayName=" + disPlayName), userLogin()).andDo(result -> {
            Response<UserModel> response = getResponse(result, USER_MODEL_TYPE);
            assertEquals(SUCCESS.getCode(), response.getCode());
            UserModel u = response.getResult();
            assertEquals(desc, u.getDesc());
            assertEquals(disPlayName, u.getDisplayName());
            assertNotNull(u.getId());
        });
    }

    @Test
    public void getUserInfo() throws Exception {
        getMockData(get("/user/userInfo"), adminLogin()).andDo(result -> {
            Response<UserModel> response = getResponse(result, USER_MODEL_TYPE);
            assertEquals(SUCCESS.getCode(), response.getCode());
            UserModel u = response.getResult();
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

        // mock 实现类
        mockInjectInstance(userService, "qiniuService", new TestQiNiuServiceImpl());

        MockMultipartFile file = new MockMultipartFile("file", "logo.png", MediaType.IMAGE_PNG_VALUE, inputStream);
        getMockData(multipart("/user/imgUpload").file(file), userLogin()).andDo(result -> {
            Response<Object> response = getResponse(result, OBJECT_TYPE);
            assertEquals(SUCCESS.getCode(), response.getCode());
            assertNotNull(response.getResult());
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
    public void emailVerify() throws Exception {
        String email = randomStr(4) + "@celess.cn";
        String pwd = StringUtil.getMD5("123456789");
        userMapper.addUser(new User(email, pwd));
        String verifyId = randomStr();
        LoginReq req = new LoginReq(email, "123456789", true);
        redisUtil.setEx(email + "-verify", verifyId, 2, TimeUnit.DAYS);
        getMockData(post("/emailVerify").param("verifyId", verifyId).param("email", email), login(req)).andDo(result ->
                assertEquals(SUCCESS.getCode(), getResponse(result, OBJECT_TYPE).getCode())
        );
    }

    @Test
    public void resetPwd() throws Exception {
        String email = randomStr(4) + "@celess.cn";
        String pwd = StringUtil.getMD5("1234567890");
        userMapper.addUser(new User(email, pwd));
        LoginReq req = new LoginReq(email, "1234567890", true);
        String verifyId = randomStr();
        // 设置验证id
        redisUtil.setEx(email + "-resetPwd", verifyId, 2, TimeUnit.DAYS);
        MockHttpServletRequestBuilder resetPwd = post("/resetPwd").param("verifyId", verifyId).param("email", email).param("pwd", "123456789");
        //  未验证
        getMockData(resetPwd, login(req)).andDo(result -> assertEquals(USEREMAIL_NOT_VERIFY.getCode(), getResponse(result, OBJECT_TYPE).getCode()));
        // 设置未验证
        userMapper.updateEmailStatus(email, true);
        // 正常
        getMockData(resetPwd, login(req)).andDo(result -> assertEquals(SUCCESS.getCode(), getResponse(result, OBJECT_TYPE).getCode()));
    }

    @Test
    public void multipleDelete() throws Exception {
        List<User> userList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            String s = randomStr();
            String email = s.substring(s.length() - 4) + "@celess.cn";
            String pwd = StringUtil.getMD5("123456789");
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
        List<Integer> idList = userList.stream().map(user -> user.getId().intValue()).collect(Collectors.toList());
        getMockData(delete("/admin/user/delete"), adminLogin(), idList).andDo(result -> {
            Response<List<Map<String, Object>>> response = getResponse(result, USER_MODEL_LIST_TYPE);
            assertEquals(SUCCESS.getCode(), response.getCode());
            response.getResult().forEach(o -> {
                // 判断响应数据中是否包含输入的id
                assertTrue(idList.contains((int) o.get("id")));
                // 判断处理状态
                boolean status = (boolean) o.get("status");
                if (o.containsKey("msg"))
                    assertFalse(status);
                else
                    assertTrue(status);
            });
        });

    }

    @Test
    public void updateInfoByAdmin() throws Exception {
        UserReq userReq = new UserReq();
        String email = randomStr(4) + "@celess.cn";
        User user = new User(email, StringUtil.getMD5("123456789"));
        userMapper.addUser(user);
        User userByDb = userMapper.findByEmail(email);
        userReq.setId(userByDb.getId());
        userReq.setPwd(randomStr().substring(0, 10));
        userReq.setDesc(randomStr());
        userReq.setEmailStatus(new Random().nextBoolean());
        userReq.setRole("admin");
        userReq.setDisplayName(randomStr(4));
        userReq.setEmail(randomStr(5) + "@celess.cn");
        getMockData(put("/admin/user"), adminLogin(), userReq).andDo(result -> {
            Response<UserModel> response = getResponse(result, USER_MODEL_TYPE);
            assertEquals(SUCCESS.getCode(), response.getCode());
            UserModel userModel = response.getResult();
            assertEquals(userReq.getId(), userModel.getId());
            assertEquals(userReq.getRole(), userModel.getRole());
            assertEquals(userReq.getEmail(), userModel.getEmail());
            assertEquals(userReq.getDesc(), userModel.getDesc());
            assertEquals(userReq.getDisplayName(), userModel.getDisplayName());
        });
    }

    @Test
    public void getAllUser() throws Exception {
        getMockData(get("/admin/users?page=1&count=10"), adminLogin()).andDo(result -> {
            Response<PageData<UserModel>> response = getResponse(result, USER_MODEL_PAGE_TYPE);
            assertEquals(SUCCESS.getCode(), response.getCode());
            // 结果集非空
            assertNotNull(response.getResult());
            // 判断pageInfo是否包装完全
            PageData<UserModel> pageData = response.getResult();
            assertNotEquals(0, pageData.getTotal());
            assertEquals(1, pageData.getPageNum());
            assertEquals(10, pageData.getPageSize());
            // 内容完整
            for (UserModel u : pageData.getList()) {
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
        String email = randomStr(4) + "@celess.cn";
        getMockData(get("/emailStatus/" + email)).andDo(result -> assertFalse((Boolean) getResponse(result, BOOLEAN_TYPE).getResult()));
        getMockData(get("/emailStatus/" + ADMIN_EMAIL)).andDo(result -> assertTrue((Boolean) getResponse(result, BOOLEAN_TYPE).getResult()));
    }

    @Test
    public void setPwd() throws Exception {
        String email = randomStr(4) + "@celess.cn";
        assertEquals(1, userMapper.addUser(new User(email, StringUtil.getMD5("1234567890"))));
        LoginReq req = new LoginReq(email, "1234567890", false);
        String token = login(req);
        assertNotNull(token);
        MultiValueMap<String, String> param = new LinkedMultiValueMap<String, String>();
        param.add("pwd", "1234567890");
        param.add("newPwd", "aaabbbccc");
        param.add("confirmPwd", "aaabbbccc");
        getMockData(post("/user/setPwd").params(param), token).andDo(result -> {
            assertEquals(SUCCESS.getCode(), getResponse(result).getCode());
            assertEquals(StringUtil.getMD5("aaabbbccc"), userMapper.getPwd(email));
        });
    }
}