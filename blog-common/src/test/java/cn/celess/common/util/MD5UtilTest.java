package cn.celess.common.util;

import cn.celess.common.CommonBaseTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MD5UtilTest extends CommonBaseTest {

    @Test
    public void getMD5() {
        assertEquals("25f9e794323b453885f5181f1b624d0b", MD5Util.getMD5("123456789"));
    }
}