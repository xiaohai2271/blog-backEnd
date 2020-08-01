package cn.celess.blog.util;

import cn.celess.blog.BaseTest;
import cn.celess.blog.enmu.ResponseEnum;
import cn.celess.blog.entity.Response;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class HttpUtilTest extends BaseTest {

    @Test
    public void get() {
        String s = HttpUtil.get("https://api.celess.cn/headerInfo");
        assertNotNull(s);
        Response<Map<String, Object>> response = getResponse(s, MAP_OBJECT_TYPE);
        assertEquals(ResponseEnum.SUCCESS.getCode(), response.getCode());
        assertNotEquals(0, response.getResult().size());
    }
}