package cn.celess.blog.filter;

import cn.celess.blog.BaseTest;
import cn.celess.blog.enmu.ResponseEnum;
import cn.celess.blog.entity.Response;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

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
        getMockData(MockMvcRequestBuilders.get("/counts").session(session)).andDo(result -> {
            Response<Object> response = getResponse(result);
            Assert.assertEquals(expectResponse.getCode(), response.getCode());
            if (msg.length != 0) {
                Assert.assertEquals(msg[0], response.getMsg());
            }
        });
    }
}
