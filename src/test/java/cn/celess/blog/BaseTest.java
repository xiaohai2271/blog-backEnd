package cn.celess.blog;


import cn.celess.blog.entity.Response;
import cn.celess.blog.entity.model.UserModel;
import cn.celess.blog.entity.request.LoginReq;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

import static cn.celess.blog.enmu.ResponseEnum.SUCCESS;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @Author: 小海
 * @Date: 2019/08/22 12:46
 * @Description: 测试基类
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class BaseTest {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected MockMvc mockMvc;
    protected final static String Code = "code";
    protected final static String Result = "result";
    private static String userToken = null;
    private static String adminToken = null;
    /**
     * jackson 序列化/反序列化Json
     */
    protected final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private WebApplicationContext wac;
    protected MockHttpSession session;

    @Before
    public void before() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        session = new MockHttpSession();
        System.out.println("==========> 开始测试 <=========");
    }

    @After
    public void after() {
        System.out.println("==========> 测试结束 <=========");
    }

    /**
     * 　admin 权限用户登录
     *
     * @return token
     */
    protected String adminLogin() {
        if (adminToken != null) return adminToken;
        LoginReq req = new LoginReq();
        req.setEmail("a@celess.cn");
        req.setPassword("123456789");
        req.setIsRememberMe(false);
        adminToken = login(req);
        assertNotNull(adminToken);
        return adminToken;
    }

    /**
     * user 权限用户登录
     *
     * @return token
     */
    protected String userLogin() {
        if (userToken != null) return userToken;
        LoginReq req = new LoginReq();
        req.setEmail("zh56462271@qq.com");
        req.setPassword("123456789");
        req.setIsRememberMe(false);
        userToken = login(req);
        assertNotNull(userToken);
        return userToken;
    }

    /**
     * 登录逻辑
     *
     * @param req 用户信息
     * @return token | null
     */
    private String login(LoginReq req) {
        String str = null;
        try {
            str = getMockData(post("/login"), null, req)
                    .andReturn().getResponse().getContentAsString();
            Response<UserModel> response = mapper.readValue(str, new TypeReference<Response<UserModel>>() {
            });
            assertEquals(SUCCESS.getCode(), response.getCode());
            String token = response.getResult().getToken();
            assertNotNull(token);
            return token;
        } catch (Exception e) {
            logger.error("测试登录错误");
            e.printStackTrace();
        }
        assertNotNull(str);
        return null;
    }

    @Test
    public void test() {
        // 测试登录
        assertNotNull(userLogin());
        assertNotNull(adminLogin());
        try {
            // 测试getMockData方法
            assertNotNull(getMockData(get("/headerInfo")));
            getMockData((get("/headerInfo"))).andDo(result -> assertNotNull(getResponse(result)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 产生指定长度的随机字符
     *
     * @param len
     * @return
     */
    protected String randomStr(int len) {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, len);
    }


    /**
     * 抽离的mock请求方法
     *
     * @param builder MockHttpServletRequestBuilder ：get(...)  post(...) ....
     * @return 返回 ResultActions
     * @throws Exception exc
     */
    protected ResultActions getMockData(MockHttpServletRequestBuilder builder) throws Exception {
        return getMockData(builder, null, null);
    }

    /**
     * 抽离的mock请求方法 重载
     *
     * @param builder ..
     * @param token   用户登录的token
     * @return ..
     * @throws Exception ..
     */
    protected ResultActions getMockData(MockHttpServletRequestBuilder builder, String token) throws Exception {
        return getMockData(builder, token, null);
    }

    /**
     * 抽离的mock请求方法 重载
     *
     * @param builder ..
     * @param token   ..
     * @param content http中发送的APPLICATION_JSON的json数据
     * @return ..
     * @throws Exception ..
     */
    protected ResultActions getMockData(MockHttpServletRequestBuilder builder, String token, Object content) throws Exception {
        //        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = get(url);
        if (token != null) {
            builder.header("Authorization", token);
        }
        if (content != null) {
            builder.content(mapper.writeValueAsString(content)).contentType(MediaType.APPLICATION_JSON);
        }
        return mockMvc.perform(builder).andExpect(status().isOk());
    }

    protected <T> Response<T> getResponse(String json) {
        Response<T> response = null;
        try {
            response = mapper.readValue(json, new TypeReference<Response<T>>() {
            });
        } catch (IOException e) {
            logger.error("接续json Response对象错误，json:[{}]", json);
            e.printStackTrace();
        }
        assertNotNull(response);
        return response;
    }

    protected <T> Response<T> getResponse(MvcResult result) {
        try {
            return getResponse(result.getResponse().getContentAsString());
        } catch (UnsupportedEncodingException e) {
            logger.error("接续json Response对象错误，result:[{}]", result);
            e.printStackTrace();
        }
        return null;
    }
}
