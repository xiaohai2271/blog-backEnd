package cn.celess.common.util;

import cn.celess.common.CommonBaseTest;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertNotNull;

public class DateFormatUtilTest extends CommonBaseTest {

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