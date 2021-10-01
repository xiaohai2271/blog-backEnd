package cn.celess.visitor.util;

import cn.celess.visitor.VisitorBaseTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AddressUtilTest extends VisitorBaseTest {

    @Test
    public void getCityInfo() {
        assertEquals("0|0|0|内网IP|内网IP", AddressUtil.getCityInfo("127.0.0.1"));
        assertEquals("中国|0|上海|上海市|阿里云", AddressUtil.getCityInfo("106.15.205.190"));
    }
}