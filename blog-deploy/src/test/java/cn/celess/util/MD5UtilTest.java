package cn.celess.util;

import cn.celess.BaseTest;
import cn.celess.common.util.MD5Util;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MD5UtilTest extends BaseTest {

    @Test
    public void getMD5() {
        assertEquals("25f9e794323b453885f5181f1b624d0b", MD5Util.getMD5("123456789"));
    }
}