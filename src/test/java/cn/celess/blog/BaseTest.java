package cn.celess.blog;

import cn.celess.blog.entity.request.LoginReq;
import net.sf.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.Cookie;

import java.util.UUID;

import static org.junit.Assert.*;

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
        try {
            LoginReq req = new LoginReq();
            req.setEmail("a@celess.cn");
            req.setPassword("123456789");
            req.setIsRememberMe(false);
            JSONObject loginReq = JSONObject.fromObject(req);
            String str = mockMvc.perform(MockMvcRequestBuilders.post("/login").content(loginReq.toString()).contentType("application/json"))
                    //                    .andDo(MockMvcResultHandlers.print())
                    .andReturn().getResponse().getContentAsString();
            String token = JSONObject.fromObject(str).getJSONObject(Result).getString("token");
            assertNotNull(token);
            return token;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    protected String userLogin() {
        try {
            LoginReq req = new LoginReq();
            req.setEmail("zh56462271@qq.com");
            req.setPassword("123456789");
            req.setIsRememberMe(false);
            JSONObject loginReq = JSONObject.fromObject(req);
            String str = mockMvc.perform(MockMvcRequestBuilders.post("/login").content(loginReq.toString()).contentType("application/json"))
                    //                    .andDo(MockMvcResultHandlers.print())
                    .andReturn().getResponse().getContentAsString();
            String token = JSONObject.fromObject(str).getJSONObject(Result).getString("token");
            assertNotNull(token);
            return token;
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
}
