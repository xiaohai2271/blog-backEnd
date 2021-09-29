package cn.celess.service;

import cn.celess.BaseTest;
import cn.celess.common.entity.vo.PageData;
import cn.celess.common.entity.vo.VisitorModel;
import cn.celess.common.service.VisitorService;
import com.alibaba.druid.util.StringUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class VisitorServiceTest extends BaseTest {

    @Autowired
    VisitorService visitorService;

    @Test
    public void location() {
        assertEquals("0|0|0|内网IP|内网IP", visitorService.location("127.0.0.1"));
    }

    @Test
    public void visitorPage() {
        long start = System.currentTimeMillis();
        PageData<VisitorModel> visitorModelPageData = visitorService.visitorPage(1, 10, true);
        assertTrue(System.currentTimeMillis() - start <= 1500);
        assertTrue(visitorModelPageData.getList().stream().noneMatch(visitor -> StringUtils.isEmpty(visitor.getLocation())));
    }
}