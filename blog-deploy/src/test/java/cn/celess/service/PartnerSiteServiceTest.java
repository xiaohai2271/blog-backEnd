package cn.celess.service;

import cn.celess.BaseTest;
import cn.celess.common.entity.PartnerSite;
import cn.celess.common.entity.vo.PageData;
import cn.celess.common.service.PartnerSiteService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

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