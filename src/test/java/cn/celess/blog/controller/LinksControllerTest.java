package cn.celess.blog.controller;

import cn.celess.blog.BaseTest;
import cn.celess.blog.entity.PartnerSite;
import cn.celess.blog.entity.model.PageData;
import cn.celess.blog.entity.request.LinkApplyReq;
import cn.celess.blog.entity.request.LinkReq;
import cn.celess.blog.exception.MyException;
import cn.celess.blog.mapper.PartnerMapper;
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
    @Autowired
    PartnerSiteService partnerSiteService;


    @Test
    public void create() throws Exception {
        LinkReq linkReq = new LinkReq();
        linkReq.setName(UUID.randomUUID().toString().substring(0, 4));
        linkReq.setOpen(false);
        linkReq.setUrl("https://" + randomStr(4) + "celess.cn");
        String token = adminLogin();
        mockMvc.perform(
                post("/admin/links/create")
                        .content(JSONObject.fromObject(linkReq).toString())
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(result -> {
            JSONObject object = JSONObject.fromObject(result.getResponse().getContentAsString());
            assertEquals(SUCCESS.getCode(), object.getInt(Code));
            PartnerSite site = (PartnerSite) JSONObject.toBean(object.getJSONObject(Result), PartnerSite.class);
            assertNotNull(site.getId());
            assertEquals(linkReq.getName(), site.getName());
            assertEquals(linkReq.getUrl(), site.getUrl());
            assertEquals(linkReq.isOpen(), site.getOpen());
        });

        // https/http
        linkReq.setName(UUID.randomUUID().toString().substring(0, 4));
        linkReq.setOpen(false);
        String url = randomStr(4) + ".celess.cn";
        linkReq.setUrl(url);
        mockMvc.perform(
                post("/admin/links/create")
                        .content(JSONObject.fromObject(linkReq).toString())
                        .header("Authorization", token)
                        .contentType("application/json")
        ).andDo(result -> {
            JSONObject object = JSONObject.fromObject(result.getResponse().getContentAsString());
            assertEquals(SUCCESS.getCode(), object.getInt(Code));
            PartnerSite site = (PartnerSite) JSONObject.toBean(object.getJSONObject(Result), PartnerSite.class);
            assertEquals("http://" + url, site.getUrl());
        });

        // 测试已存在的数据
        mockMvc.perform(
                post("/admin/links/create")
                        .content(JSONObject.fromObject(linkReq).toString())
                        .header("Authorization", token)
                        .contentType("application/json")
        ).andDo(result -> assertEquals(DATA_HAS_EXIST.getCode(), JSONObject.fromObject(result.getResponse().getContentAsString()).getInt(Code)));
    }

    @Test
    public void del() throws Exception {
        PartnerSite partnerSite = new PartnerSite();
        partnerSite.setName(UUID.randomUUID().toString().substring(0, 4));
        partnerSite.setOpen(true);
        partnerSite.setDesc("");
        partnerSite.setIconPath("");
        partnerSite.setUrl("https://" + randomStr(4) + ".celess.cn");
        mapper.insert(partnerSite);
        PartnerSite lastest = mapper.getLastest();
        assertNotNull(lastest.getId());
        String token = adminLogin();
        mockMvc.perform(delete("/admin/links/del/" + lastest.getId()).header("Authorization", token)).andDo(result -> {
            JSONObject object = JSONObject.fromObject(result.getResponse().getContentAsString());
            assertEquals(SUCCESS.getCode(), object.getInt(Code));
            assertTrue(object.getBoolean(Result));
        });
        long id = lastest.getId();
        do {
            id += 1;
        } while (mapper.existsById(id));
        System.out.println("删除ID=" + id + "的数据");
        mockMvc.perform(delete("/admin/links/del/" + id).header("Authorization", token)).andDo(result ->
                assertEquals(DATA_NOT_EXIST.getCode(), JSONObject.fromObject(result.getResponse().getContentAsString()).getInt(Code))
        );
    }

    @Test
    public void update() throws Exception {
        // 增数据
        PartnerSite partnerSite = new PartnerSite();
        partnerSite.setName(UUID.randomUUID().toString().substring(0, 4));
        partnerSite.setOpen(true);
        partnerSite.setDesc("");
        partnerSite.setIconPath("");
        partnerSite.setDelete(false);
        partnerSite.setUrl("https://" + randomStr(4) + ".celess.cn");
        mapper.insert(partnerSite);
        // 查数据
        PartnerSite lastest = mapper.getLastest();
        assertNotNull(lastest.getId());
        String token = adminLogin();
        // 构建请求
        LinkReq linkReq = new LinkReq();
        linkReq.setUrl(lastest.getUrl());
        linkReq.setOpen(!lastest.getOpen());
        linkReq.setName(UUID.randomUUID().toString().substring(0, 4));
        linkReq.setId(lastest.getId());
        mockMvc.perform(
                put("/admin/links/update")
                        .content(JSONObject.fromObject(linkReq).toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token)
        ).andDo(result -> {
            JSONObject object = JSONObject.fromObject(result.getResponse().getContentAsString());
            assertEquals(SUCCESS.getCode(), object.getInt(Code));
            PartnerSite site = (PartnerSite) JSONObject.toBean(object.getJSONObject(Result), PartnerSite.class);
            assertNotNull(site.getId());
            assertEquals(linkReq.getId(), site.getId().longValue());
            assertEquals(linkReq.getUrl(), site.getUrl());
            assertEquals(linkReq.getName(), site.getName());
            assertEquals(linkReq.isOpen(), site.getOpen());
        });
    }

    @Test
    public void allForOpen() throws Exception {
        mockMvc.perform(get("/links")).andDo(result -> {
            JSONObject object = JSONObject.fromObject(result.getResponse().getContentAsString());
            assertEquals(SUCCESS.getCode(), object.getInt(Code));
            object.getJSONArray(Result).forEach(o -> {
                PartnerSite site = (PartnerSite) JSONObject.toBean(JSONObject.fromObject(o), PartnerSite.class);
                assertNotNull(site.getUrl());
                assertNull(site.getOpen());
                assertNotNull(site.getName());
            });
        });
    }

    @Test
    public void all() throws Exception {
        mockMvc.perform(get("/admin/links?page=1&count=10").header("Authorization", adminLogin())).andDo(result -> {
            JSONObject object = JSONObject.fromObject(result.getResponse().getContentAsString());
            assertEquals(SUCCESS.getCode(), object.getInt(Code));
            PageData<PartnerSite> pageData = (PageData<PartnerSite>) JSONObject.toBean(object.getJSONObject(Result), PageData.class);
            assertEquals(1, pageData.getPageNum());
            assertEquals(10, pageData.getPageSize());
            for (Object o : pageData.getList()) {
                PartnerSite site = (PartnerSite) JSONObject.toBean(JSONObject.fromObject(o), PartnerSite.class);
                assertNotNull(site.getUrl());
                assertNotNull(site.getName());
                assertNotNull(site.getOpen());
            }
        });
    }

    // 手动测试
    @Test
    public void apply() {
        // 做service 层的测试
        mockEmailServiceInstance(partnerSiteService, "mailService");
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
        mockEmailServiceInstance(partnerSiteService, "mailService");
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