package cn.celess.blog.filter;

import cn.celess.blog.BaseTest;
import net.sf.json.JSONObject;
import org.junit.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.junit.Assert.*;
import static cn.celess.blog.enmu.ResponseEnum.*;

/**
 * @Author: 小海
 * @Date: 2019/11/28 16:05
 * @Description: 授权拦截器的测试类
 */
public class AuthorizationFilter extends BaseTest {

    @Test
    public void UserAccess() throws Exception {
        String token = "";
        // 未登录
        mockMvc.perform(get("/user/userInfo").header("Authorization", token)).andDo(result -> {
            JSONObject object = JSONObject.fromObject(result.getResponse().getContentAsString());
            assertEquals(HAVE_NOT_LOG_IN.getCode(), object.getInt(Code));
        });
        token = userLogin();
        mockMvc.perform(get("/user/userInfo").header("Authorization", token)).andDo(result -> {
            JSONObject object = JSONObject.fromObject(result.getResponse().getContentAsString());
            assertEquals(SUCCESS.getCode(), object.getInt(Code));
        });
    }

    @Test
    public void AdminAccess() throws Exception {
        String token = "";
        // 未登录
        mockMvc.perform(get("/admin/articles?page=1&count=1").header("Authorization", token)).andDo(result -> {
            JSONObject object = JSONObject.fromObject(result.getResponse().getContentAsString());
            assertEquals(HAVE_NOT_LOG_IN.getCode(), object.getInt(Code));
        });
        token = userLogin();
        mockMvc.perform(get("/admin/articles?page=1&count=1").header("Authorization", token)).andDo(result -> {
            JSONObject object = JSONObject.fromObject(result.getResponse().getContentAsString());
            assertEquals(PERMISSION_ERROR.getCode(), object.getInt(Code));
        });
        token = adminLogin();
        mockMvc.perform(get("/admin/articles?page=1&count=1").header("Authorization", token)).andDo(result -> {
            JSONObject object = JSONObject.fromObject(result.getResponse().getContentAsString());
            assertEquals(SUCCESS.getCode(), object.getInt(Code));
        });
    }

    @Test
    public void VisitorAccess() throws Exception {
        mockMvc.perform(get("/user/userInfo")).andDo(result -> {
            JSONObject object = JSONObject.fromObject(result.getResponse().getContentAsString());
            assertEquals(HAVE_NOT_LOG_IN.getCode(), object.getInt(Code));
        });
        mockMvc.perform(get("/admin/articles?page=1&count=1")).andDo(result -> {
            JSONObject object = JSONObject.fromObject(result.getResponse().getContentAsString());
            assertEquals(HAVE_NOT_LOG_IN.getCode(), object.getInt(Code));
        });
    }

    @Test
    public void authorizationTest() throws Exception {
        // 测试response中有无Authorization字段
        String s = userLogin();
        mockMvc.perform(get("/user/userInfo").header("Authorization", s)).andDo(result -> {
            JSONObject object = JSONObject.fromObject(result.getResponse().getContentAsString());
            assertEquals(SUCCESS.getCode(), object.getInt(Code));
            assertNotNull(result.getResponse().getHeader("Authorization"));
            assertNotEquals(s, result.getResponse().getHeader("Authorization"));
        });
    }
}
