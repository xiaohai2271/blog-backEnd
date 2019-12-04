package cn.celess.blog.util;

import cn.celess.blog.BaseTest;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

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