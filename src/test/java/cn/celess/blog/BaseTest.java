package cn.celess.blog;


import cn.celess.blog.entity.request.LoginReq;
import net.sf.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


import java.util.UUID;

import static cn.celess.blog.enmu.ResponseEnum.SUCCESS;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    protected MockMvc mockMvc;
    protected final static String Code = "code";
    protected final static String Result = "result";
    private static String userToken = null;
    private static String adminToken = null;

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


    protected String adminLogin() {
        if (adminToken != null) return adminToken;
        try {
            LoginReq req = new LoginReq();
            req.setEmail("a@celess.cn");
            req.setPassword("123456789");
            req.setIsRememberMe(false);
            JSONObject loginReq = JSONObject.fromObject(req);
            String str = mockMvc.perform(MockMvcRequestBuilders.post("/login").content(loginReq.toString()).contentType("application/json"))
                    //                    .andDo(MockMvcResultHandlers.print())
                    .andReturn().getResponse().getContentAsString();
            adminToken = JSONObject.fromObject(str).getJSONObject(Result).getString("token");
            assertNotNull(adminToken);
            return adminToken;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    protected String userLogin() {
        if (userToken != null) return userToken;
        try {
            LoginReq req = new LoginReq();
            req.setEmail("zh56462271@qq.com");
            req.setPassword("123456789");
            req.setIsRememberMe(false);
            JSONObject loginReq = JSONObject.fromObject(req);
            String str = mockMvc.perform(MockMvcRequestBuilders.post("/login").content(loginReq.toString()).contentType("application/json"))
                    //                    .andDo(MockMvcResultHandlers.print())
                    .andReturn().getResponse().getContentAsString();
            userToken = JSONObject.fromObject(str).getJSONObject(Result).getString("token");
            assertNotNull(userToken);
            return userToken;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Test
    public void test() {

    }

    protected String randomStr(int len) {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, len);
    }


    protected ResultActions getMockData(String url) throws Exception {
        return getMockData(url, null, null);
    }

    protected ResultActions getMockData(String url, String token) throws Exception {
        return getMockData(url, token, null);
    }

    protected ResultActions getMockData(String url, String token, Object content) throws Exception {
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = get(url);
        if (token != null) {
            mockHttpServletRequestBuilder.header("Authorization", token);
        }
        if (content != null) {
            mockHttpServletRequestBuilder.content(JSONObject.fromObject(content).toString()).contentType(MediaType.APPLICATION_JSON);
        }
        return mockMvc.perform(mockHttpServletRequestBuilder).andExpect(status().isOk());
    }
}
