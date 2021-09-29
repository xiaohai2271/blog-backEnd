package cn.celess.util;

import cn.celess.BaseTest;
import cn.celess.common.util.DateFormatUtil;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertNotNull;

public class DateFormatUtilTest extends BaseTest {

    @Test
    public void get() {
        assertNotNull(DateFormatUtil.get(new Date()));
    }

    @Test
    public void getForXmlDate() {
        assertNotNull(DateFormatUtil.getForXmlDate(new Date()));
    }

    @Test
    public void getNow() {
        assertNotNull(DateFormatUtil.getNow());
    }
}