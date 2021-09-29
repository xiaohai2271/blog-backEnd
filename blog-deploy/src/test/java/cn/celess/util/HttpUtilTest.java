package cn.celess.util;

import cn.celess.BaseTest;
import cn.celess.common.util.HttpUtil;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class HttpUtilTest extends BaseTest {

    @Test
    public void get() {
        String s = HttpUtil.get("https://api.celess.cn/headerInfo");
        assertNotNull(s);
        //        Response<Map<String, Object>> response = getResponse(s, MAP_OBJECT_TYPE);
        //        assertEquals(ResponseEnum.SUCCESS.getCode(), response.getCode());
        //        assertNotEquals(0, response.getResult().size());
    }
}