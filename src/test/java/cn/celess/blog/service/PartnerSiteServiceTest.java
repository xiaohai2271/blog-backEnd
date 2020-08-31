package cn.celess.blog.service;

import cn.celess.blog.BaseTest;
import cn.celess.blog.entity.PartnerSite;
import cn.celess.blog.entity.model.PageData;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.Assert.*;

public class PartnerSiteServiceTest extends BaseTest {

    @Autowired
    PartnerSiteService partnerSiteService;

    @Test
    public void partnerSitePages() {
        // 测试deleted 参数
        PageData<PartnerSite> pageData = partnerSiteService.partnerSitePages(1, 10, true);
        assertTrue(pageData.getList().stream().allMatch(PartnerSite::getDelete));
        pageData = partnerSiteService.partnerSitePages(1, 10, false);
        assertTrue(pageData.getList().stream().noneMatch(PartnerSite::getDelete));
        pageData = partnerSiteService.partnerSitePages(1, 10, null);

        List<PartnerSite> list = pageData.getList();
        assertNotEquals(0, list.stream().filter(PartnerSite::getDelete).count());
        assertNotEquals(0, list.stream().filter(partnerSite -> !partnerSite.getDelete()).count());
    }
}