package cn.celess.blog.controller;

import cn.celess.blog.BaseTest;
import cn.celess.blog.entity.PartnerSite;
import cn.celess.blog.entity.Response;
import cn.celess.blog.entity.model.PageData;
import cn.celess.blog.entity.request.LinkApplyReq;
import cn.celess.blog.entity.request.LinkReq;
import cn.celess.blog.exception.MyException;
import cn.celess.blog.mapper.PartnerMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import cn.celess.blog.service.MailService;
import cn.celess.blog.service.PartnerSiteService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mail.SimpleMailMessage;

import java.lang.reflect.Field;
import java.util.UUID;

import static cn.celess.blog.enmu.ResponseEnum.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@Slf4j
public class LinksControllerTest extends BaseTest {

    @Autowired
    PartnerMapper mapper;
    private static final TypeReference<?> LINK_MODEL_TYPE = new TypeReference<Response<PartnerSite>>() {
    };
    private static final TypeReference<?> LINK_MODEL_LIST_TYPE = new TypeReference<Response<List<PartnerSite>>>() {
    };
    private static final TypeReference<?> LINK_MODEL_PAGE_TYPE = new TypeReference<Response<PageData<PartnerSite>>>() {
    };
    @Autowired
    PartnerSiteService partnerSiteService;


    @Test
    public void create() throws Exception {
        LinkReq linkReq = new LinkReq();
        linkReq.setName(randomStr(4));
        linkReq.setOpen(false);
        linkReq.setUrl("https://" + randomStr(4) + "example.com");
        getMockData(post("/admin/links/create"), adminLogin(), linkReq).andDo(result -> {
            Response<PartnerSite> response = getResponse(result, LINK_MODEL_TYPE);
            assertEquals(SUCCESS.getCode(), response.getCode());
            PartnerSite site = response.getResult();
            assertNotNull(site.getId());
            assertEquals(linkReq.getName(), site.getName());
            assertEquals(linkReq.getUrl(), site.getUrl());
            assertEquals(linkReq.isOpen(), site.getOpen());
        });

        // https/http
        linkReq.setName(randomStr(4));
        linkReq.setOpen(false);
        linkReq.setUrl(randomStr(4) + ".example.com");
        getMockData(post("/admin/links/create"), adminLogin(), linkReq).andDo(result -> {
            Response<PartnerSite> response = getResponse(result, LINK_MODEL_TYPE);
            assertEquals(SUCCESS.getCode(), response.getCode());
            PartnerSite site = response.getResult();
            assertEquals("http://" + linkReq.getUrl(), site.getUrl());
        });

        // 测试已存在的数据
        getMockData(post("/admin/links/create"), adminLogin(), linkReq).andDo(result ->
                assertEquals(DATA_HAS_EXIST.getCode(), getResponse(result, STRING_TYPE).getCode())
        );
    }

    @Test
    public void del() throws Exception {
        PartnerSite partnerSite = new PartnerSite();
        partnerSite.setName(randomStr(4));
        partnerSite.setOpen(true);
        partnerSite.setDesc("");
        partnerSite.setIconPath("");
        partnerSite.setUrl("https://" + randomStr(4) + ".celess.cn");
        mapper.insert(partnerSite);
        PartnerSite latest = mapper.getLastest();
        assertNotNull(latest.getId());
        getMockData(delete("/admin/links/del/" + latest.getId()), adminLogin()).andDo(result -> {
            Response<Boolean> response = getResponse(result, BOOLEAN_TYPE);
            assertEquals(SUCCESS.getCode(), response.getCode());
            assertTrue(response.getResult());
        });
        long id = latest.getId();
        do {
            id += 1;
        } while (mapper.existsById(id));
        System.out.println("删除ID=" + id + "的数据");
        getMockData(delete("/admin/links/del/" + id), adminLogin()).andDo(result ->
                assertEquals(DATA_NOT_EXIST.getCode(), getResponse(result, STRING_TYPE).getCode())
        );
    }

    @Test
    public void update() throws Exception {
        // 增数据
        PartnerSite partnerSite = new PartnerSite();
        partnerSite.setName(randomStr(4));
        partnerSite.setOpen(true);
        partnerSite.setDesc("");
        partnerSite.setIconPath("");
        partnerSite.setDelete(false);
        partnerSite.setUrl("https://" + randomStr(5) + ".celess.cn");
        mapper.insert(partnerSite);
        // 查数据
        PartnerSite latest = mapper.getLastest();
        assertNotNull(latest.getId());
        // 构建请求
        LinkReq linkReq = new LinkReq();
        linkReq.setUrl(latest.getUrl());
        linkReq.setOpen(!latest.getOpen());
        linkReq.setName(randomStr(4));
        linkReq.setId(latest.getId());

        getMockData(put("/admin/links/update"), adminLogin(), linkReq).andDo(result -> {
            Response<PartnerSite> response = getResponse(result, LINK_MODEL_TYPE);
            assertEquals(SUCCESS.getCode(), response.getCode());
            PartnerSite site = response.getResult();
            assertNotNull(site.getId());
            assertEquals(linkReq.getId(), site.getId().longValue());
            assertEquals(linkReq.getUrl(), site.getUrl());
            assertEquals(linkReq.getName(), site.getName());
            assertEquals(linkReq.isOpen(), site.getOpen());
        });
    }

    @Test
    public void allForOpen() throws Exception {
        getMockData(get("/links")).andDo(result -> {
            Response<List<PartnerSite>> response = getResponse(result, LINK_MODEL_LIST_TYPE);
            assertEquals(SUCCESS.getCode(), response.getCode());
            response.getResult().forEach(site -> {
                assertNotNull(site.getUrl());
                assertNull(site.getOpen());
                assertNotNull(site.getName());
            });
        });
    }

    @Test
    public void all() throws Exception {
        getMockData(get("/admin/links?page=1&count=10"), adminLogin()).andDo(result -> {
            Response<PageData<PartnerSite>> response = getResponse(result, LINK_MODEL_PAGE_TYPE);
            assertEquals(SUCCESS.getCode(), response.getCode());
            PageData<PartnerSite> pageData = response.getResult();
            assertEquals(1, pageData.getPageNum());
            assertEquals(10, pageData.getPageSize());
            for (PartnerSite site : pageData.getList()) {
                assertNotNull(site.getUrl());
                assertNotNull(site.getName());
                assertNotNull(site.getOpen());
            }
        });
    }

    @Test
    public void apply() {
        // 做service 层的测试
        //        mockEmailServiceInstance(partnerSiteService, "mailService");
        mockInjectInstance(partnerSiteService, "mailService", new TestMailServiceImpl());
        LinkApplyReq req = new LinkApplyReq();
        req.setName(randomStr(4));
        req.setUrl("https://" + randomStr(4) + ".celess.cn");
        req.setIconPath("https://www.celess.cn/example.png");
        req.setDesc("desc :" + randomStr());
        req.setEmail(randomStr(4) + "@celess.cn");
        req.setLinkUrl(req.getUrl() + "/links");
        try {
            // 抓取不到数据的链接
            partnerSiteService.apply(req);
        } catch (MyException e) {
            log.debug("测试抓取不到数据");
            assertEquals(CANNOT_GET_DATA.getCode(), e.getCode());
        }

        req.setLinkUrl("https://bing.com");
        req.setUrl(req.getLinkUrl());
        try {
            partnerSiteService.apply(req);
        } catch (MyException e) {
            log.debug("测试未添加本站链接的友链申请");
            assertEquals(APPLY_LINK_NO_ADD_THIS_SITE.getCode(), e.getCode());
            assertNotNull(e.getResult());
            try {
                // 测试uuid一致性
                log.debug("测试uuid一致性");
                partnerSiteService.apply(req);
            } catch (MyException e2) {
                assertEquals(e.getResult(), e2.getResult());
            }
        }
        log.debug("测试正常申请");
        req.setLinkUrl("https://www.celess.cn");
        req.setUrl(req.getLinkUrl());
        PartnerSite apply = partnerSiteService.apply(req);
        assertNotNull(apply);
        assertNotNull(apply.getId());
    }

    @Test
    public void reapply() {
        //mockEmailServiceInstance(partnerSiteService, "mailService");
        try {
            partnerSiteService.reapply(randomStr());
            throw new AssertionError();
        } catch (MyException e) {
            assertEquals(DATA_EXPIRED.getCode(), e.getCode());
        }

        LinkApplyReq req = new LinkApplyReq();
        req.setName(randomStr(4));
        req.setIconPath("https://www.celess.cn/example.png");
        req.setDesc("desc :" + randomStr());
        req.setEmail(randomStr(4) + "@celess.cn");
        req.setLinkUrl("https://bing.com");
        req.setUrl(req.getLinkUrl());
        String uuid = null;
        try {
            partnerSiteService.apply(req);
            // err here
            throw new AssertionError();
        } catch (MyException e) {
            uuid = (String) e.getResult();
            String reapply = partnerSiteService.reapply(uuid);
            assertEquals(reapply, "success");
        }

        try {
            partnerSiteService.reapply(uuid);
            throw new AssertionError();
        } catch (MyException e) {
            assertEquals(DATA_EXPIRED.getCode(), e.getCode());
        }
    }
}