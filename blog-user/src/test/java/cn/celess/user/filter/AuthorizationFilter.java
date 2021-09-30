package cn.celess.user.filter;

import cn.celess.common.entity.Response;
import cn.celess.user.UserBaseTest;
import org.junit.Test;

import static cn.celess.common.enmu.ResponseEnum.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

/**
 * @Author: 小海
 * @Date: 2019/11/28 16:05
 * @Description: 授权拦截器的测试类
 */
public class AuthorizationFilter extends UserBaseTest {

    @Test
    public void UserAccess() throws Exception {
        // 未登录
        getMockData(get("/user/userInfo")).andDo(result -> assertEquals(HAVE_NOT_LOG_IN.getCode(), getResponse(result).getCode()));
        // user权限登录
        getMockData(get("/user/userInfo"), userLogin()).andDo(result -> assertEquals(SUCCESS.getCode(), getResponse(result).getCode()));
    }

    @Test
    public void AdminAccess() throws Exception {
        // 未登录
        getMockData(get("/admin/articles?page=1&count=1")).andDo(result -> assertEquals(HAVE_NOT_LOG_IN.getCode(), getResponse(result).getCode()));
        // user权限
        getMockData(get("/admin/articles?page=1&count=1"), userLogin()).andDo(result -> assertEquals(PERMISSION_ERROR.getCode(), getResponse(result).getCode()));
        // admin 权限
        getMockData(get("/admin/articles?page=1&count=1"), adminLogin()).andDo(result -> assertEquals(SUCCESS.getCode(), getResponse(result).getCode()));
    }

    @Test
    public void VisitorAccess() throws Exception {
        getMockData(get("/user/userInfo")).andDo(result -> assertEquals(HAVE_NOT_LOG_IN.getCode(), getResponse(result).getCode()));
        getMockData(get("/admin/articles?page=1&count=1")).andDo(result -> assertEquals(HAVE_NOT_LOG_IN.getCode(), getResponse(result).getCode()));
    }

    @Test
    public void authorizationTest() throws Exception {
        // 测试response中有无Authorization字段
        String token = userLogin();
        getMockData(get("/user/userInfo"), token).andDo(result -> {
            Response<Object> response = getResponse(result);
            assertEquals(SUCCESS.getCode(), response.getCode());
            assertNotNull(result.getResponse().getHeader("Authorization"));
            assertNotEquals(token, result.getResponse().getHeader("Authorization"));
        });
    }
}
