package cn.celess.blog;


import cn.celess.blog.entity.Response;
import cn.celess.blog.entity.model.FileInfo;
import cn.celess.blog.entity.model.FileResponse;
import cn.celess.blog.entity.model.UserModel;
import cn.celess.blog.entity.request.LoginReq;
import cn.celess.blog.service.MailService;
import cn.celess.blog.service.FileManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mail.SimpleMailMessage;
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
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    protected final static String USERE_MAIL = "zh56462271@qq.com";
    protected final static String ADMIN_EMAIL = "a@celess.cn";

    /**
     * jackson 序列化/反序列化Json
     */
    protected final ObjectMapper mapper = new ObjectMapper();
    protected static final TypeReference<?> BOOLEAN_TYPE = new TypeReference<Response<Boolean>>() {
    };
    protected static final TypeReference<?> STRING_TYPE = new TypeReference<Response<String>>() {
    };
    protected static final TypeReference<?> OBJECT_TYPE = new TypeReference<Response<Object>>() {
    };
    protected static final TypeReference<?> MAP_OBJECT_TYPE = new TypeReference<Response<Map<String, Object>>>() {
    };

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
        LoginReq req = new LoginReq();
        req.setEmail(ADMIN_EMAIL);
        req.setPassword("123456789");
        req.setIsRememberMe(true);
        String token = login(req);
        assertNotNull(token);
        return token;
    }

    /**
     * user 权限用户登录
     *
     * @return token
     */
    protected String userLogin() {
        LoginReq req = new LoginReq();
        req.setEmail(USERE_MAIL);
        req.setPassword("123456789");
        req.setIsRememberMe(true);
        String token = login(req);
        assertNotNull(token);
        return token;
    }

    /**
     * 登录逻辑
     *
     * @param req 用户信息
     * @return token | null
     */
    protected String login(LoginReq req) {
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
        assertNotEquals(userLogin(), adminLogin());
        try {
            // 测试getMockData方法
            assertNotNull(getMockData(get("/headerInfo")));
            getMockData((get("/headerInfo"))).andDo(result -> assertNotNull(getResponse(result, OBJECT_TYPE)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 产生指定长度的随机字符
     *
     * @param len len
     * @return str
     */
    protected String randomStr(int len) {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, len);
    }

    /**
     * 产生指定长度的随机字符
     *
     * @return str
     */
    protected String randomStr() {
        return UUID.randomUUID().toString();
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
            logger.debug("param::json->{}", mapper.writeValueAsString(content));
        }
        return mockMvc.perform(builder).andExpect(status().isOk());
    }


    protected <T> Response<T> getResponse(String json) {
        return getResponse(json, OBJECT_TYPE);
    }

    protected <T> Response<T> getResponse(MvcResult result) {
        return getResponse(result, OBJECT_TYPE);
    }

    /**
     * 根据json 信息反序列化成Response对象
     *
     * @param json json
     * @param <T>  泛型
     * @return Response对象
     */
    protected <T> Response<T> getResponse(String json, TypeReference<?> responseType) {
        Response<T> response = null;
        try {
            response = mapper.readValue(json, responseType);
        } catch (IOException e) {
            logger.error("解析json Response对象错误，json:[{}]", json);
            e.printStackTrace();
        }
        assertNotNull(response);
        return response;
    }

    /**
     * 根据json 信息反序列化成Response对象
     *
     * @param result MvcResult
     * @param <T>    泛型
     * @return Response对象
     */
    protected <T> Response<T> getResponse(MvcResult result, TypeReference<?> responseType) {
        try {
            return getResponse(result.getResponse().getContentAsString(), responseType);
        } catch (UnsupportedEncodingException e) {
            logger.error("解析json Response对象错误，result:[{}]", result);
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 修改 mailService 的实现类
     *
     * @param service       service 类
     * @param mailFiledName service 中自动注入的mailService字段名
     */
    protected void mockInjectInstance(Object service, String mailFiledName, Object impl) {
        Field field;
        try {
            assertNotNull(service);
            field = service.getClass().getDeclaredField(mailFiledName);
            field.setAccessible(true);
            field.set(service, impl);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    @Slf4j
    public static class TestMailServiceImpl implements MailService {

        @Override
        public Boolean AsyncSend(SimpleMailMessage message) {
            log.debug("异步邮件请求,SimpleMailMessage:[{}]", getJson(message));
            return true;
        }

        @Override
        public Boolean send(SimpleMailMessage message) {
            log.debug("邮件请求,SimpleMailMessage:[{}]", getJson(message));
            return true;
        }

        /**
         * 转json
         *
         * @param o
         * @return
         */
        private String getJson(Object o) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                return mapper.writeValueAsString(o);
            } catch (JsonProcessingException e) {
                return null;
            }
        }
    }

    @Slf4j
    public static class TestQiniuFileServiceImpl implements FileManager {
        @Override
        public FileResponse uploadFile(InputStream is, String fileName) {
            FileResponse response = new FileResponse();
            log.debug("上传文件请求，[fileName:{}]", fileName);

            response.key = "key";
            response.bucket = "bucket";
            response.hash = "hash";
            response.size = 1;
            return response;
        }

        @Override
        public List<FileInfo> getFileList() {
            log.debug("获取文件列表请求");
            return new ArrayList<>();
        }

        @Override
        public boolean deleteFile(String fileName) {
            log.debug("删除[{}]成功", fileName);
            return true;
        }
    }
}
