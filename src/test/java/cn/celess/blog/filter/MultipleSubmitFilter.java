package cn.celess.blog.filter;

import cn.celess.blog.BaseTest;
import cn.celess.blog.enmu.ResponseEnum;
import net.sf.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import javax.servlet.http.Cookie;

/**
 * @Author: 小海
 * @Date: 2019/11/28 16:17
 * @Description: 测试重复请求
 */
public class MultipleSubmitFilter extends BaseTest {

    private MockHttpSession session = null;

    @Test
    public void submitTest() throws Exception {
        session = new MockHttpSession();
        sendRequest(ResponseEnum.SUCCESS);
        sendRequest(ResponseEnum.FAILURE, "重复请求");
    }


    private void sendRequest(ResponseEnum expectResponse, String... msg) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/counts").session(session)).andDo(result -> {
            JSONObject object = JSONObject.fromObject(result.getResponse().getContentAsString());
            Assert.assertEquals(expectResponse.getCode(), object.getInt(Code));
            if (msg.length != 0) {
                Assert.assertEquals(msg[0], object.getString("msg"));
            }
        });
    }
}
