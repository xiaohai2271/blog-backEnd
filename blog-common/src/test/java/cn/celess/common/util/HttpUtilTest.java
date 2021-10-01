package cn.celess.common.util;

import cn.celess.common.CommonBaseTest;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class HttpUtilTest extends CommonBaseTest {

    @Test
    public void get() {
        String s = HttpUtil.getHttpResponse("https://api.celess.cn/headerInfo");
        assertNotNull(s);
        //        Response<Map<String, Object>> response = getResponse(s, MAP_OBJECT_TYPE);
        //        assertEquals(ResponseEnum.SUCCESS.getCode(), response.getCode());
        //        assertNotEquals(0, response.getResult().size());
    }
}